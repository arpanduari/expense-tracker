package dev.arpan.expensetracker.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author arpan
 * @since 8/13/25
 */
@Service
@RequiredArgsConstructor
public class OtpProducer {
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.queue.otp}")
    private String otpQueueName;

    public void sendOtpMessage(String email, String otp) {
        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("email", email);
        payLoad.put("otp", otp);
        rabbitTemplate.convertAndSend(otpQueueName, payLoad);
    }
}
