package dev.arpan.expensetracker.user;

import dev.arpan.expensetracker.user.dto.UserDto;
import dev.arpan.expensetracker.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author arpan
 * @since 8/17/25
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login details: " + username));
        return UserMapper.toUserDto(user);
    }
}
