package dev.expensewise.backend.user.util;

import dev.expensewise.backend.config.security.CustomUserDetails;
import dev.expensewise.backend.user.User;
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
    public User createUserWithId(Long userId) {
        return User.builder()
                .id(userId)
                .build();
    }

    public Long getUserId(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUser().getId();
    }
}
