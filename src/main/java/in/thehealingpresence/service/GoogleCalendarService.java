package in.thehealingpresence.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import in.thehealingpresence.config.properties.GoogleCalendarProperties;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.OAuthToken;
import in.thehealingpresence.repository.OAuthTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * One-way push of receptionist bookings to Upma's Google Calendar.
 *
 * <p>Flow:
 * <ol>
 *   <li>Admin visits {@code /admin/google-calendar/connect} once → consents on Google.</li>
 *   <li>{@link #exchangeCode(String)} stores the long-lived refresh token in {@link OAuthToken}.</li>
 *   <li>{@link #pushEvent(BookingRequest)} uses the refresh token (auto-minting fresh access tokens)
 *       to create a calendar event whenever a RECEPTIONIST booking is saved.</li>
 * </ol>
 *
 * <p>If {@code google.calendar.client-id} is unset (env var missing in dev), the service short-circuits:
 * {@link #isConfigured()} returns false and pushes become no-ops. This lets the app boot and run end-to-end
 * locally before the Google Cloud project has been set up.
 */
@Service
public class GoogleCalendarService {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class);
    private static final String PROVIDER_KEY = "google-calendar";
    private static final String APPLICATION_NAME = "The Healing Presence";
    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final OAuthTokenRepository tokenRepository;
    private final GoogleCalendarProperties props;

    public GoogleCalendarService(OAuthTokenRepository tokenRepository,
                                 GoogleCalendarProperties props) {
        this.tokenRepository = tokenRepository;
        this.props = props;
    }

    /** True if Google Cloud credentials are present in env. */
    public boolean isConfigured() {
        return props.isConfigured();
    }

    /** True if Upma has completed the OAuth consent flow. */
    public boolean isConnected() {
        return isConfigured()
                && tokenRepository.findByProvider(PROVIDER_KEY)
                .map(t -> t.getRefreshToken() != null && !t.getRefreshToken().isBlank())
                .orElse(false);
    }

    public Optional<OAuthToken> getStoredToken() {
        return tokenRepository.findByProvider(PROVIDER_KEY);
    }

    // ----------------------------------------------------------- OAuth dance --

    /** URL to redirect Upma to so she can consent. */
    public String buildAuthorizationUrl() {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Google Calendar credentials not set. Define GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET env vars.");
        }
        List<String> scopes = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
        try {
            GoogleAuthorizationCodeFlow flow = buildFlow(scopes);
            AuthorizationCodeRequestUrl url = flow.newAuthorizationUrl()
                    .setRedirectUri(props.redirectUri())
                    .setAccessType("offline")        // need a refresh token
                    .set("prompt", "consent");       // force refresh_token even if previously granted
            return url.build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build Google authorization URL: " + e.getMessage(), e);
        }
    }

    /** Exchange the authorization code from /admin/google-calendar/callback for tokens. */
    @Transactional
    public void exchangeCode(String code) {
        if (!isConfigured()) {
            throw new IllegalStateException("Google Calendar credentials not set.");
        }
        try {
            TokenResponse response = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    props.clientId(),
                    props.clientSecret(),
                    code,
                    props.redirectUri()
            ).execute();

            OAuthToken token = tokenRepository.findByProvider(PROVIDER_KEY)
                    .orElseGet(() -> new OAuthToken(PROVIDER_KEY));
            if (response.getRefreshToken() != null) {
                token.setRefreshToken(response.getRefreshToken());
            }
            token.setAccessToken(response.getAccessToken());
            if (response.getExpiresInSeconds() != null) {
                token.setExpiryAt(Instant.now().plusSeconds(response.getExpiresInSeconds()));
            }
            token.setScope(response.getScope());
            tokenRepository.save(token);
            log.info("Stored Google Calendar refresh token (scope={})", response.getScope());

            // G9: verify the configured calendar id actually resolves with this token.
            // A typo in GOOGLE_CALENDAR_ID would otherwise silently fail every push.
            try {
                Calendar calendar = buildCalendarClient();
                calendar.calendars().get(props.calendarId()).execute();
                log.info("Verified Google Calendar id '{}' is reachable.", props.calendarId());
            } catch (Exception verifyEx) {
                log.warn("Google Calendar id '{}' could not be verified: {}", props.calendarId(), verifyEx.getMessage());
                throw new IllegalStateException(
                        "Calendar id '" + props.calendarId() + "' is not accessible with the consenting user's account. "
                                + "Check GOOGLE_CALENDAR_ID. Original error: " + verifyEx.getMessage(), verifyEx);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to exchange Google authorization code: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------- push event --

    /**
     * Create a Google Calendar event for the given booking.
     * @return the Google event ID (caller persists onto {@link BookingRequest#getGoogleEventId()})
     *         or {@code null} if not configured / not connected (no-op).
     */
    public String pushEvent(BookingRequest booking) {
        if (!isConnected()) {
            log.debug("Google Calendar not connected; skipping push for booking {}", booking.getId());
            return null;
        }
        try {
            Calendar calendar = buildCalendarClient();

            Event event = new Event()
                    .setSummary(booking.getName() + " — " + nullSafe(booking.getTherapyType()))
                    .setDescription(buildDescription(booking))
                    .setStart(toEventDateTime(booking.getSlotStart()))
                    .setEnd(toEventDateTime(booking.getSlotEnd()));

            Event created = calendar.events().insert(props.calendarId(), event).execute();
            log.info("Pushed booking {} to Google Calendar as event {}", booking.getId(), created.getId());
            return created.getId();
        } catch (Exception e) {
            log.warn("Failed to push booking {} to Google Calendar: {}", booking.getId(), e.getMessage());
            return null;
        }
    }

    /** Delete an existing event (used by cancellation flow). */
    public void deleteEvent(String eventId) {
        if (!isConnected() || eventId == null || eventId.isBlank()) {
            return;
        }
        try {
            Calendar calendar = buildCalendarClient();
            calendar.events().delete(props.calendarId(), eventId).execute();
            log.info("Deleted Google Calendar event {}", eventId);
        } catch (Exception e) {
            log.warn("Failed to delete Google Calendar event {}: {}", eventId, e.getMessage());
        }
    }

    // ----------------------------------------------------------- helpers --

    private GoogleAuthorizationCodeFlow buildFlow(List<String> scopes) throws GeneralSecurityException, IOException {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                .setClientId(props.clientId())
                .setClientSecret(props.clientSecret());
        // Web-application OAuth flow only — no .setInstalled().
        GoogleClientSecrets secrets = new GoogleClientSecrets().setWeb(details);
        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                secrets,
                scopes
        ).setAccessType("offline").build();
    }

    private Calendar buildCalendarClient() throws GeneralSecurityException, IOException {
        OAuthToken stored = tokenRepository.findByProvider(PROVIDER_KEY)
                .orElseThrow(() -> new IllegalStateException("No Google refresh token stored — admin must connect first."));

        Credential credential = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .setClientSecrets(props.clientId(), props.clientSecret())
                .build()
                .setRefreshToken(stored.getRefreshToken());

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    private static EventDateTime toEventDateTime(java.time.LocalDateTime ldt) {
        ZonedDateTime zoned = ldt.atZone(IST);
        com.google.api.client.util.DateTime dt =
                new com.google.api.client.util.DateTime(java.util.Date.from(zoned.toInstant()));
        return new EventDateTime().setDateTime(dt).setTimeZone(IST.getId());
    }

    private static String buildDescription(BookingRequest b) {
        StringBuilder sb = new StringBuilder();
        sb.append("Client: ").append(b.getName()).append('\n');
        sb.append("Email: ").append(b.getEmail()).append('\n');
        sb.append("Phone: ").append(nullSafe(b.getPhone())).append('\n');
        sb.append("Therapy: ").append(nullSafe(b.getTherapyType())).append('\n');
        sb.append("Duration: ").append(b.getDurationHours()).append("h\n");
        if (b.getNotes() != null && !b.getNotes().isBlank()) {
            sb.append('\n').append("Notes:\n").append(b.getNotes()).append('\n');
        }
        sb.append('\n').append("— Pushed from The Healing Presence reception panel");
        return sb.toString();
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }
}
