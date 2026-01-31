package dev.expensewise.backend.common.service;

/**
 * @author arpan
 * @since 10/6/25
 */
public interface MailService {
    void sendMail(String toEmail, String subject, String content);
}
