package com.healingpresence.repository;

import com.healingpresence.domain.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    Optional<OAuthToken> findByProvider(String provider);
}
