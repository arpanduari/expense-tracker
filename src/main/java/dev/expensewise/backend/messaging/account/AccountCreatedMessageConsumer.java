package dev.expensewise.backend.messaging.account;

import dev.expensewise.backend.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/24/25
 */
@Service
@RequiredArgsConstructor
public class AccountCreatedMessageConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${app.queue.account-created}")
    public void receiveAccountCreatedMessage(Map<String, String> message) {
        String email = message.get("email");
        String username = message.get("username");
        String loginUrl = message.get("loginUrl");
        emailService.sendAccountCreatedSuccessMail(email, username, loginUrl);
    }
}
