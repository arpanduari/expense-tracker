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
public class ChangePasswordMessageProducer {
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.queue.change-password}")
    private String changePasswordQueueName;

    public void sendChangePasswordMessage(String email, String loginLink) {
        Map<String, String> message = Map.of("email", email, "loginUrl", loginLink);
        rabbitTemplate.convertAndSend(changePasswordQueueName, message);
    }
}
