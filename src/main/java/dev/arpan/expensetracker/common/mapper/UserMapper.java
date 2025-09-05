package dev.arpan.expensetracker.common.mapper;

import dev.arpan.expensetracker.auth.dto.RegisterRequestDTO;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.dto.UserDto;

/**
 * @author arpan
 * @since 8/3/25
 */
public final class UserMapper {
    private UserMapper() {
    }

    public static User toUser(RegisterRequestDTO registerRequestDTO) {
        return User.builder()
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(registerRequestDTO.getPassword())
                .currency(registerRequestDTO.getCurrency())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .currency(user.getCurrency())
                .avatarUrl(user.getSecureUrl())
                .build();
    }
}
