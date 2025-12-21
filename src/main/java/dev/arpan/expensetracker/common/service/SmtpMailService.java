package dev.arpan.expensetracker.common.service;

import dev.arpan.expensetracker.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author arpan
 * @since 10/6/25
 */
@Service
@Profile("dev")
@RequiredArgsConstructor
public class SmtpMailService implements MailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(toEmail);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send email", e);
        }
    }
}
