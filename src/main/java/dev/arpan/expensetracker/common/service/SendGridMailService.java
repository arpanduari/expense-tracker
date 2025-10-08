package dev.arpan.expensetracker.common.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author arpan
 * @since 10/6/25
 */
@Service
@Profile("prod")
@RequiredArgsConstructor
public class SendGridMailService implements MailService{
    private final SendGrid sendGrid;
    private final SendGridConfigurationProperties sendGridConfigProperties;

    @Override
    public void sendMail(String toEmail, String subject, String content) {
        Email from = new Email(sendGridConfigProperties.getFromEmail());
        Email to = new Email(toEmail);
        Content htmlEmailContent = new Content("text/html", content);
        Mail mail = new Mail(from, subject, to, htmlEmailContent);
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        }catch (IOException ex){
            throw new RuntimeException("Failed to send email.", ex);
        }
    }
}
