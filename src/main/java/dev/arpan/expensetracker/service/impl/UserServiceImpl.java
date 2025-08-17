package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.dto.UserDto;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.mapper.UserMapper;
import dev.arpan.expensetracker.repository.UserRepository;
import dev.arpan.expensetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author arpan
 * @since 8/17/25
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login details: " + username));
        return UserMapper.toUserDto(user);
    }
}
