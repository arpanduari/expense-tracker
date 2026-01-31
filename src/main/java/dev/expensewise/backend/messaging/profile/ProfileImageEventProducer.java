package dev.expensewise.backend.messaging.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author arpan
 * @since 8/30/25
 */
@Service
@RequiredArgsConstructor
public class ProfileImageEventProducer {
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.queue.profile-event}")
    private String profileEventQueueName;

    public void sendProfileImageEvent(String username) {
        rabbitTemplate.convertAndSend(profileEventQueueName, username);
    }
}
