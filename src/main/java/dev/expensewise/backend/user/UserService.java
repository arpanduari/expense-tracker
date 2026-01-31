package dev.expensewise.backend.user;

import dev.expensewise.backend.common.mapper.UserMapper;
import dev.expensewise.backend.user.dto.UserResponse;
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

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login details: " + username));
        return UserMapper.toUserResponse(user);
    }
}
