package dev.arpan.expensetracker.utils;

import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author arpan
 * @since 8/4/25
 */
@Component
@RequiredArgsConstructor
public final class UserUtil {
    public static User createUserWithId(Long userId) {
        return User.builder()
                .id(userId)
                .build();
    }

    public static Long getUserId(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUser().getId();
    }
}
