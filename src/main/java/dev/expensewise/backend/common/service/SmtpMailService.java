package dev.expensewise.backend.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author arpan
 * @since 10/6/25
 */
@Slf4j
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
            log.error("Error sending email to {}: {}", toEmail, e.getLocalizedMessage());
        }
    }
}
