package dev.arpan.expensetracker.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/22/25
 */
@Service
@RequiredArgsConstructor
public class ResetSuccessMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.queue.reset-password}")
    private String queueName;

    public void sendResetSuccessMessage(String email, String link) {
        Map<String, String> payLoad = Map.of("email", email, "link", link);
        rabbitTemplate.convertAndSend(queueName, payLoad);
    }
}
