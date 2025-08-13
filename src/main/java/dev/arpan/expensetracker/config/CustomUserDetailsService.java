package dev.arpan.expensetracker.config;

import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.repository.UserRepository;
import dev.arpan.expensetracker.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author arpan
 * @since 8/3/25
 */
@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login details: " + username));
        return new CustomUserDetails(user);
    }
}
