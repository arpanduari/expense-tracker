package dev.expensewise.backend.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author arpan
 * @since 8/13/25
 */
@Getter
@Component
public class JWTConfig {
    @Value("${JWT_SECRET}")
    private String jwtSecret;
}
