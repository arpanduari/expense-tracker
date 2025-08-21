package dev.arpan.expensetracker.service;

/**
 * @author arpan
 * @since 8/5/25
 */
public interface EmailService {
    void sendOtp(String toEmail, String otp);
    void sendForgotPassword(String toEmail, String link);
    void sendEmail(String toEmail, String subject, String content);
}
