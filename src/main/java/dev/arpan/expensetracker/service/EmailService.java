package dev.arpan.expensetracker.service;

/**
 * @author arpan
 * @since 8/5/25
 */
public interface EmailService {
    public void sendOtp(String toEmail, String otp);
}
