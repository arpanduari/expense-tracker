package dev.expensewise.backend.auth.util;

import dev.expensewise.backend.config.security.JWTConfig;
import dev.expensewise.backend.constants.application.ApplicationConstants;
import dev.expensewise.backend.constants.security.JWTConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * @author arpan
 * @since 8/3/25
 */
@Service
@RequiredArgsConstructor
public class JWTService {
    private final Environment environment;
    private final JWTConfig jwtConfig;

    private SecretKey getSecretKey() {
        String secret = environment.getProperty(JWTConstants.JWT_SECRET, jwtConfig.getJwtSecret());
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, Long userId) {
        return Jwts.builder()
                .issuer(JWTConstants.JWT_ISSUER)
                .subject(JWTConstants.JWT_SUBJECT)
                .claim("username", username)
                .claim(ApplicationConstants.USER_ID, userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String username, Long userId) {
        return Jwts.builder()
                .issuer(JWTConstants.JWT_ISSUER)
                .subject(JWTConstants.JWT_REFRESH_SUBJECT)
                .claim("username", username)
                .claim("userId", userId)
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .compact();
    }

    public Claims parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String subject = claims.get("sub", String.class);
        if (!Objects.equals(subject, JWTConstants.JWT_SUBJECT)) {
            throw new RuntimeException("Invalid accessToken");
        }
        return claims;
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
