package dev.arpan.expensetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author arpan
 * @since 8/13/25
 */
@Component
public class JWTConfig {
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String getJwtSecret() {
        return jwtSecret;
    }

}
