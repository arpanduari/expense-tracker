package dev.arpan.expensetracker.messaging.profile;

import dev.arpan.expensetracker.profile.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author arpan
 * @since 8/30/25
 */
@Service
@RequiredArgsConstructor
public class DefaultProfileImageEventConsumer {
    private final ProfileImageService profileImageService;

    @RabbitListener(queues = "${app.queue.profile-event}")
    public void receiveProfileImageEvent(String username) {
        profileImageService.addDefaultProfileImage(username);
    }

}
