package dev.arpan.expensetracker.mapper;

import dev.arpan.expensetracker.dto.ProfileImageEvent;
import dev.arpan.expensetracker.dto.RegisterRequestDTO;
import dev.arpan.expensetracker.dto.UserDto;
import dev.arpan.expensetracker.entity.User;

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

    public static ProfileImageEvent toProfileImageEvent(User user) {
        return ProfileImageEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .publicId(user.getPublicId())
                .secureUrl(user.getSecureUrl())
                .build();
    }
}
