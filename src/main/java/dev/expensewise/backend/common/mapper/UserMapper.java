package dev.expensewise.backend.common.mapper;

import dev.expensewise.backend.auth.dto.RegisterRequest;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.dto.UserResponse;

/**
 * @author arpan
 * @since 8/3/25
 */
public final class UserMapper {
    private UserMapper() {
    }

    public static User toUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(registerRequest.password())
                .currency(registerRequest.currency())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getUsername(), user.getEmail(), user.getCurrency(), user.getSecureUrl());
    }
}
