package dev.arpan.expensetracker.utils;

import dev.arpan.expensetracker.entity.PasswordResetToken;
import dev.arpan.expensetracker.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 8/20/25
 */
@Component
@RequiredArgsConstructor
public class ForgotPasswordUtil {
    private final PasswordEncoder passwordEncoder;

    public PasswordResetToken buildResetPasswordRequest(String token, User user) {
        return PasswordResetToken.builder()
                .user(user)
                .tokenHash(passwordEncoder.encode(token))
                .createdAt(LocalDateTime.now())
                .expiryTime(LocalDateTime.now().plusHours(1L))
                .build();
    }
}
