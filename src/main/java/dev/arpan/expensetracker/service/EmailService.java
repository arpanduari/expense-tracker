package dev.arpan.expensetracker.service;

/**
 * @author arpan
 * @since 8/5/25
 */
public interface EmailService {
    void sendOtp(String toEmail, String otp);

    void sendForgotPassword(String toEmail, String link);

    void sendResetPassword(String toEmail, String link);

    void sendChangePasswordSuccessMail(String toEmail, String loginLink);

    void sendAccountCreatedSuccessMail(String toEmail, String username, String loginLink);

    void sendEmail(String toEmail, String subject, String content);

}
