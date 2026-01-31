package dev.expensewise.backend.messaging.auth;

import dev.expensewise.backend.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/19/25
 */
@Service
@RequiredArgsConstructor
public class ForgotPasswordMessageConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${app.queue.forgot-password}")
    public void receiveForgotPasswordMessage(Map<String, String> message) {
        String email = message.get("email");
        String link = message.get("link");
        String username = message.get("username");
        emailService.sendForgotPassword(email, link, username);
    }
}
