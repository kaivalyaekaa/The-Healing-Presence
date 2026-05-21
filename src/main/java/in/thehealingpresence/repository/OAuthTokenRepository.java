package in.thehealingpresence.repository;

import in.thehealingpresence.domain.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    Optional<OAuthToken> findByProvider(String provider);
}
