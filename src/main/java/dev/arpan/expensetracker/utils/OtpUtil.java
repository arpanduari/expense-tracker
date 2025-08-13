package dev.arpan.expensetracker.utils;

import dev.arpan.expensetracker.entity.OtpVerification;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author arpan
 * @since 8/5/25
 */
public final class OtpUtil {
    private static final int OTP_LENGTH = 6;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private OtpUtil() {
    }

    public static String createOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(SECURE_RANDOM.nextInt(10));
        }
        return otp.toString();
    }
    public static OtpVerification createOtpVerification(String email) {
        return OtpVerification.builder()
                .otp(createOtp())
                .email(email)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .token(UUID.randomUUID().toString())
                .verified(false)
                .build();
    }
}
