package dev.arpan.expensetracker.common.mapper;

import dev.arpan.expensetracker.auth.dto.RegisterRequest;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.dto.UserResponse;

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
