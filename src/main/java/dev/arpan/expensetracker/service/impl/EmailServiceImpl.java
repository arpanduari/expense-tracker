package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.service.EmailService;
import dev.arpan.expensetracker.service.EmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;

    @Override
    public void sendOtp(String toEmail, String otp, String username) {
        try {
            Map<String, String> data = Map.of("OTP", otp, "USER_NAME", username);
            String emailContent = emailTemplateService.getEmailContent("otp-page.html", data);
            sendEmail(toEmail, "ExpenseWise - OTP", emailContent);
        } catch (IOException ex) {
            throw new RuntimeException("Error while generating otp. Please try again later.");
        }
    }

    @Override
    public void sendForgotPassword(String toEmail, String link, String username) {
        try {
            Map<String, String> data = Map.of("RESET_LINK", link, "USER_NAME", username);
            String emailContent = emailTemplateService.getEmailContent("forgot-password.html", data);
            sendEmail(toEmail, "ExpenseWise - Forgot Password", emailContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendResetPassword(String toEmail, String link, String username) {
        try {
            Map<String, String> data = Map.of("LOGIN_URL", link, "USER_NAME", username);
            String emailContent = emailTemplateService.getEmailContent("reset-success.html", data);
            sendEmail(toEmail, "ExpenseWise - Reset Password", emailContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendChangePasswordSuccessMail(String toEmail, String loginLink) {
        try {
            Map<String, String> data = Map.of("LOGIN_URL", loginLink);
            String emailContent = emailTemplateService.getEmailContent("change-success.html", data);
            sendEmail(toEmail, "ExpenseWise - Password Changed", emailContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAccountCreatedSuccessMail(String toEmail, String username, String loginUrl) {
        try {
            Map<String, String> data = Map.of("USER_NAME", username, "LOGIN_URL", loginUrl);
            String emailContent = emailTemplateService.getEmailContent("create-success.html", data);
            sendEmail(toEmail, "ExpenseWise - Account Created", emailContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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
