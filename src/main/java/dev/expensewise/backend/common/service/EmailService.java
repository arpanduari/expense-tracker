package dev.expensewise.backend.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    private final MailService mailService;
    private final EmailTemplateService emailTemplateService;

    public void sendOtp(String toEmail, String otp, String username) {
        Map<String, String> data = Map.of("otp", otp, "username", username);
        String emailContent = emailTemplateService.getEmailContent("otp-page.jte", data);
        sendEmail(toEmail, "ExpenseWise - OTP", emailContent);
    }


    public void sendForgotPassword(String toEmail, String link, String username) {
        Map<String, String> data = Map.of("resetLink", link, "username", username);
        String emailContent = emailTemplateService.getEmailContent("forgot-password.jte", data);
        sendEmail(toEmail, "ExpenseWise - Forgot Password", emailContent);
    }


    public void sendResetPassword(String toEmail, String link, String username) {
        Map<String, String> data = Map.of("loginUrl", link, "username", username);
        String emailContent = emailTemplateService.getEmailContent("reset-success.jte", data);
        sendEmail(toEmail, "ExpenseWise - Reset Password", emailContent);
    }


    public void sendChangePasswordSuccessMail(String toEmail, String loginLink) {
        Map<String, String> data = Map.of("loginUrl", loginLink);
        String emailContent = emailTemplateService.getEmailContent("change-success.jte", data);
        sendEmail(toEmail, "ExpenseWise - Password Changed", emailContent);
    }


    public void sendAccountCreatedSuccessMail(String toEmail, String username, String loginUrl) {
        Map<String, String> data = Map.of("username", username, "loginUrl", loginUrl);
        String emailContent = emailTemplateService.getEmailContent("create-success.jte", data);
        sendEmail(toEmail, "ExpenseWise - Account Created", emailContent);
    }


    public void sendEmail(String toEmail, String subject, String content) {
        mailService.sendMail(toEmail, subject, content);
    }

}
