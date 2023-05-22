package com.example.moyiza_be.common.security.jwt.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByEmail(String userEmail);

    void deleteByEmail(String userEmail);
}