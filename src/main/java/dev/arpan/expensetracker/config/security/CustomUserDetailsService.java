package dev.arpan.expensetracker.config.security;

import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author arpan
 * @since 8/3/25
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
        User user = userIdentifier.contains("@") ?
                getUserByUserIdentifier(userIdentifier, "email", userRepository::findByEmail)
                : getUserByUserIdentifier(userIdentifier, "username", userRepository::findByUsername);
        return new CustomUserDetails(user);
    }

    public User getUserByUserIdentifier(String userIdentifier, String field, Function<String, Optional<User>> userFinder) {
        return userFinder.apply(userIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with %s: %s".formatted(field, userIdentifier)));
    }
}
