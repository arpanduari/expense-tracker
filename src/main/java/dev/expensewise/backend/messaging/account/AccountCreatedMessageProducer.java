package dev.expensewise.backend.messaging.account;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/24/25
 */
@Service
@RequiredArgsConstructor
public class AccountCreatedMessageProducer {
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.queue.account-created}")
    private String accountCreatedQueueName;

    public void sendAccountCreatedMessage(String email, String username, String loginUrl) {
        Map<String, String> payLoad = Map.of("email", email, "username", username, "loginUrl", loginUrl);
        rabbitTemplate.convertAndSend(accountCreatedQueueName, payLoad);
    }
}
