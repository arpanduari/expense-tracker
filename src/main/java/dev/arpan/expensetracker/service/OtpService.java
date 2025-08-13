package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.OtpResendResponse;
import dev.arpan.expensetracker.dto.VerifyResponse;

/**
 * @author arpan
 * @since 8/5/25
 */
public interface OtpService {

    void sendOtp(String toEmail, String otp);

    VerifyResponse verifyOtp(String token, String otp);
    OtpResendResponse resendOtp(String toEmail);
}
