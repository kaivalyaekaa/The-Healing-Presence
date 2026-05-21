package in.thehealingpresence.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Typed bindings for the {@code google.calendar.*} block in {@code application.yml}.
 *
 * <p>Blank credentials are deliberately allowed at boot — the dev workflow runs
 * without {@code GOOGLE_CLIENT_ID} / {@code GOOGLE_CLIENT_SECRET} set, and the
 * calendar adapter degrades gracefully via its own {@code isConfigured()} check.
 * Strict validation (e.g. {@code @NotBlank}) would break local boot.
 *
 * @param clientId     OAuth 2.0 web-application client id from Google Cloud Console.
 * @param clientSecret OAuth 2.0 client secret (paired with {@link #clientId}).
 * @param calendarId   Target calendar id; defaults to {@code primary} (the
 *                     authenticated user's own calendar). Override for shared calendars.
 * @param redirectUri  Where Google redirects after consent. Must match the URI
 *                     registered in the Google Cloud project's OAuth client.
 */
@ConfigurationProperties(prefix = "google.calendar")
public record GoogleCalendarProperties(
        String clientId,
        String clientSecret,
        String calendarId,
        String redirectUri
) {
    public GoogleCalendarProperties {
        // Defensive defaults so a missing yml key doesn't surface as a NullPointerException.
        if (clientId == null) clientId = "";
        if (clientSecret == null) clientSecret = "";
        if (calendarId == null || calendarId.isBlank()) calendarId = "primary";
        if (redirectUri == null || redirectUri.isBlank())
            redirectUri = "http://localhost:8080/admin/google-calendar/callback";
    }

    /** True only when both client credentials are present. The calendar adapter no-ops otherwise. */
    public boolean isConfigured() {
        return !clientId.isBlank() && !clientSecret.isBlank();
    }
}
