package dev.arpan.expensetracker.utils;

import dev.arpan.expensetracker.config.JWTConfig;
import dev.arpan.expensetracker.constants.ApplicationConstants;
import dev.arpan.expensetracker.constants.JWTConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author arpan
 * @since 8/3/25
 */
@Component
@RequiredArgsConstructor
public class JWTUtil {
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
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean shouldRefreshToken(Date expiration) {
        long remainingTime = expiration.getTime() - System.currentTimeMillis();
        return remainingTime < (5 * 60 * 1000);
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
