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
import java.time.Year;
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
    public void sendOtp(String toEmail, String otp) {
        try {
            Map<String, String> data = Map.of("OTP", otp, "YEAR", Year.now().getValue() + "");
            String emailContent = emailTemplateService.getEmailContent("otp-page.html", data);
            sendEmail(toEmail, "ExpenseWise - OTP", emailContent);
        } catch (IOException ex) {
            throw new RuntimeException("Error while generating otp. Please try again later.");
        }
    }

    @Override
    public void sendForgotPassword(String toEmail, String link) {
        try {
            Map<String, String> data = Map.of("RESET_LINK", link);
            String emailContent = emailTemplateService.getEmailContent("forgot-password.html", data);
            sendEmail(toEmail, "ExpenseWise - Forgot Password", emailContent);
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
