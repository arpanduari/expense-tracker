package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.*;

/**
 * @author arpan
 * @since 8/3/25
 */
public interface AuthService {
    RegisterResponse createUser(RegisterRequestDTO registerRequestDTO);

    RefreshResponseDTO refreshToken(RefreshRequest refreshRequest);

    LoginResponseDTO login(LoginRequestDTO loginRequest);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

    ChangePasswordResponse changePassword(Long userId, ChangePasswordRequest changePasswordRequest);
}

