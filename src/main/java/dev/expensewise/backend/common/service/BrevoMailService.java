package dev.expensewise.backend.common.service;

import brevo.ApiException;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailReplyTo;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author arpan
 * @since 12/22/25
 */
@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class BrevoMailService implements MailService {

    private final TransactionalEmailsApi brevoMailApi;

    @Value("${app.config.mail.from}")
    private String fromEmail;

    @Value("${app.config.mail.replyTo}")
    private String replyTo;

    @Override
    public void sendMail(String toEmail, String subject, String content) {
        SendSmtpEmail email = new SendSmtpEmail()
                .sender(new SendSmtpEmailSender().email(fromEmail))
                .to(List.of(new SendSmtpEmailTo().email(toEmail)))
                .replyTo(new SendSmtpEmailReplyTo().email(replyTo))
                .subject(subject)
                .htmlContent(content);
        try {
            brevoMailApi.sendTransacEmail(email);
        } catch (ApiException ex) {
            log.error("Error sending email to {}: {}", toEmail, ex.getResponseBody());
        }
    }
}
