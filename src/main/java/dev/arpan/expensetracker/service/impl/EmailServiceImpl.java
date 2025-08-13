package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.service.EmailService;
import dev.arpan.expensetracker.utils.EmailTemplateUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Async
    @Override
    public void sendOtp(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            String emailContent = EmailTemplateUtil.getOtpEmailContent(otp);
            helper.setText(emailContent, true);
            helper.setSubject("Expense Tracker OTP");
//            helper.setFrom("arpanduari26@gmail.com");
            helper.setTo(toEmail);

            mailSender.send(message);
        } catch (IOException ex) {
            throw new RuntimeException("Error while generating otp. Please try again later.");
        } catch (MessagingException ex) {
            throw new RuntimeException("Error while sending otp. Please try again later.");
        }

    }
}
