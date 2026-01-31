package dev.expensewise.backend.messaging.auth;

import dev.expensewise.backend.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/13/25
 */
@Service
@RequiredArgsConstructor
public class OtpConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${app.queue.otp}")
    public void receiveOtpMessage(Map<String, String> message) {
        String email = message.get("email");
        String otp = message.get("otp");
        String username = message.get("username");
        emailService.sendOtp(email, otp, username);
    }
}
