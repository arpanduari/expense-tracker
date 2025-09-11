package dev.arpan.expensetracker.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    public static final String EMAIL_FOLDER = "emails/";

    public void sendOtp(String toEmail, String otp, String username) {
        Map<String, String> data = Map.of("otp", otp, "username", username);
        String emailContent = emailTemplateService.getEmailContent(EMAIL_FOLDER + "otp-page.jte", data);
        sendEmail(toEmail, "ExpenseWise - OTP", emailContent);
    }


    public void sendForgotPassword(String toEmail, String link, String username) {
        Map<String, String> data = Map.of("resetLink", link, "username", username);
        String emailContent = emailTemplateService.getEmailContent(EMAIL_FOLDER + "forgot-password.jte", data);
        sendEmail(toEmail, "ExpenseWise - Forgot Password", emailContent);
    }


    public void sendResetPassword(String toEmail, String link, String username) {
        Map<String, String> data = Map.of("loginUrl", link, "username", username);
        String emailContent = emailTemplateService.getEmailContent(EMAIL_FOLDER + "reset-success.jte", data);
        sendEmail(toEmail, "ExpenseWise - Reset Password", emailContent);
    }


    public void sendChangePasswordSuccessMail(String toEmail, String loginLink) {
        Map<String, String> data = Map.of("loginUrl", loginLink);
        String emailContent = emailTemplateService.getEmailContent(EMAIL_FOLDER + "change-success.jte", data);
        sendEmail(toEmail, "ExpenseWise - Password Changed", emailContent);
    }


    public void sendAccountCreatedSuccessMail(String toEmail, String username, String loginUrl) {
        Map<String, String> data = Map.of("username", username, "loginUrl", loginUrl);
        String emailContent = emailTemplateService.getEmailContent(EMAIL_FOLDER + "create-success.jte", data);
        sendEmail(toEmail, "ExpenseWise - Account Created", emailContent);
    }


    public void sendEmail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(toEmail);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
