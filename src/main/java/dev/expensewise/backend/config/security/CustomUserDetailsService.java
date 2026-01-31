package dev.expensewise.backend.config.security;

import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author arpan
 * @since 8/3/25
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    @Override
    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
        if (userIdentifier == null || userIdentifier.isBlank()) {
            throw new UsernameNotFoundException("Username/Email cannot be blank");
        }
        User user = isEmail(userIdentifier) ? findByEmail(userIdentifier) : findByUsername(userIdentifier);
        return new CustomUserDetails(user);
    }

    private boolean isEmail(String userIdentifier) {
        return EMAIL_PATTERN.matcher(userIdentifier).matches();
    }

    private User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
