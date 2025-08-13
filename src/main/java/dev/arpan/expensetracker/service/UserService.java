package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.*;

/**
 * @author arpan
 * @since 8/3/25
 */
public interface UserService {
    RegisterResponse createUser(RegisterRequestDTO registerRequestDTO);
    UserDto getUserByUsername(String username);
    RefreshResponseDTO refreshToken(RefreshRequest refreshRequest);
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}

