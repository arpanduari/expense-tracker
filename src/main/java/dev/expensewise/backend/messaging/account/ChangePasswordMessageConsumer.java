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
public class ChangePasswordMessageConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${app.queue.change-password}")
    public void receiveChangePassword(Map<String, String> message) {
        String email = message.get("email");
        String loginLink = message.get("loginUrl");
        emailService.sendChangePasswordSuccessMail(email, loginLink);
    }
}
