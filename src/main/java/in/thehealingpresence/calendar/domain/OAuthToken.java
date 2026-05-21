package in.thehealingpresence.calendar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Persisted OAuth2 refresh token for a third-party provider (Google Calendar, etc.).
 * Currently only one row per provider — the admin user signs in once via OAuth2 and the
 * refresh token is reused for every Google Calendar push.
 */
@Entity
@Table(name = "oauth_tokens")
public class OAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** e.g. {@code "google-calendar"}. */
    @Column(nullable = false, unique = true, length = 60)
    private String provider;

    /** Long-lived refresh token. Used to mint new short-lived access tokens. */
    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    /** Current short-lived access token, if cached. */
    @Column(columnDefinition = "TEXT")
    private String accessToken;

    /** When the cached access token expires. */
    private Instant expiryAt;

    /** Granted scope (space-separated URLs from Google). */
    @Column(length = 500)
    private String scope;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public OAuthToken() {
    }

    public OAuthToken(String provider) {
        this.provider = provider;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public Instant getExpiryAt() { return expiryAt; }
    public void setExpiryAt(Instant expiryAt) { this.expiryAt = expiryAt; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
