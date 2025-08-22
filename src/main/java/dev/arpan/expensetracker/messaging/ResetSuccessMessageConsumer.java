package dev.arpan.expensetracker.messaging;

import dev.arpan.expensetracker.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author arpan
 * @since 8/22/25
 */
@Service
@RequiredArgsConstructor
public class ResetSuccessMessageConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${app.queue.reset-password}")
    public void receiveResetSuccessMessage(Map<String, String> message) {
        String email = message.get("email");
        String link = message.get("link");
        emailService.sendResetPassword(email, link);
    }
}
