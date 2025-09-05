package dev.arpan.expensetracker.auth;

import dev.arpan.expensetracker.auth.dto.*;
import dev.arpan.expensetracker.constants.application.ApplicationConstants;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.exception.PasswordNotMatchingException;
import dev.arpan.expensetracker.exception.PasswordPolicyViolationException;
import dev.arpan.expensetracker.exception.PasswordResetTokenAlreadySentException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.common.mapper.UserMapper;
import dev.arpan.expensetracker.messaging.account.ChangePasswordMessageProducer;
import dev.arpan.expensetracker.messaging.auth.ForgotPasswordMessageProducer;
import dev.arpan.expensetracker.messaging.account.ResetSuccessMessageProducer;
import dev.arpan.expensetracker.user.UserRepository;
import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.auth.util.ForgotPasswordUtil;
import dev.arpan.expensetracker.auth.util.JWTUtil;
import dev.arpan.expensetracker.auth.util.OtpUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author arpan
 * @since 8/3/25
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final ForgotPasswordUtil forgotPasswordUtil;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ForgotPasswordMessageProducer forgotPasswordMessageProducer;
    private final ResetSuccessMessageProducer resetSuccessMessageProducer;
    private final ChangePasswordMessageProducer changePasswordMessageProducer;

    @Value("${app.frontend.path}")
    private String frontendPath;

    public RegisterResponse createUser(RegisterRequestDTO registerRequestDTO) {
        User user = UserMapper.toUser(registerRequestDTO);

        isValidPassword(user.getUsername(), user.getEmail(), registerRequestDTO.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        OtpVerification otpVerification = OtpUtil.createOtpVerification(savedUser.getEmail());
        otpVerificationRepository.save(otpVerification);
        otpService.sendOtp(registerRequestDTO.getEmail(), otpVerification.getOtp(), savedUser.getUsername());
        return RegisterResponse.builder()
                .message("User registered successfully. Please check your email for verification.")
                .verificationUrl("/verify?token=" + otpVerification.getToken())
                .build();
    }


    public RefreshResponseDTO refreshToken(RefreshRequest refreshRequest) {
        try {
            Claims claims = jwtUtil.parseToken(refreshRequest.getRefreshToken());
            if (jwtUtil.isTokenExpired(claims.getExpiration())) {
                return null;
            }
            String username = claims.get("username", String.class);
            Long userId = claims.get(ApplicationConstants.USER_ID, Long.class);
            String newAccessToken = jwtUtil.generateAccessToken(username, userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(username, userId);
            return RefreshResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } catch (Exception ex) {
            return null;
        }
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserIdentifier(),
                        loginRequest.getPassword())
        );
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        String accessToken = jwtUtil.generateAccessToken(authentication.getName(), userId);
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName(), userId);
        return LoginResponseDTO.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(authentication.getName())
                .build();
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email",
                        forgotPasswordRequest.getEmail()));

        passwordResetTokenRepository.findByUserIdAndExpiryTimeAfter(user.getId(), LocalDateTime.now())
                .ifPresent(token -> {
                    Duration duration = Duration.between(LocalDateTime.now(), token.getExpiryTime());
                    throw new PasswordResetTokenAlreadySentException("Password reset request already sent." +
                            " Retry After %d minutes."
                                    .formatted(duration.toMinutes()));
                });

        String uuid = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId())
                .map(existing -> {
                            existing.setTokenHash(passwordEncoder.encode(uuid));
                            existing.setExpiryTime(LocalDateTime.now().plusHours(1L));
                            existing.setCreatedAt(LocalDateTime.now());
                            return existing;
                        }
                ).orElseGet(() -> forgotPasswordUtil.buildResetPasswordRequest(uuid, user));

        passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);

        String link = frontendPath + "/reset-password?token=" + uuid + "&id=" + passwordResetToken.getId();

        forgotPasswordMessageProducer.sendForgotPasswordMessage(user.getEmail(), link, user.getUsername());

        return ForgotPasswordResponse.builder()
                .message("Password reset link sent to your email")
                .build();
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(resetPasswordRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Password reset token", "id", resetPasswordRequest.getId() + ""));
        if (!passwordEncoder.matches(resetPasswordRequest.getToken(), passwordResetToken.getTokenHash())) {
            throw new ResourceNotFoundException("Password reset token", "id", resetPasswordRequest.getId() + "");
        }
        User user = userRepository.findById(passwordResetToken.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", passwordResetToken.getUser().getId() + ""));
        isValidPassword(user.getUsername(), user.getEmail(), resetPasswordRequest.getNewPassword());
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);

        String loginPath = frontendPath + "/login";
        resetSuccessMessageProducer.sendResetSuccessMessage(user.getEmail(), loginPath, user.getUsername());

        passwordResetTokenRepository.delete(passwordResetToken);
        return ResetPasswordResponse.builder()
                .message("Password reset successful")
                .build();
    }

    public ChangePasswordResponse changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId + ""));

        isValidPassword(user.getUsername(), user.getEmail(), changePasswordRequest.getNewPassword());

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatchingException("Old password does not match");
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new PasswordNotMatchingException("New password cannot be same as old password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        String loginPath = frontendPath + "/login";
        changePasswordMessageProducer.sendChangePasswordMessage(user.getEmail(), loginPath);

        return ChangePasswordResponse.builder()
                .isPasswordChanged(true)
                .message("Password changed successfully")
                .build();
    }

    public void isValidPassword(String username, String email, String newPassword) {
        if (newPassword.length() < 8) {
            throw new PasswordPolicyViolationException("Password must be at least 8 characters long");
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            throw new PasswordPolicyViolationException("Password must contain at least one uppercase letter");
        }
        if (!newPassword.matches(".*[a-z].*")) {
            throw new PasswordPolicyViolationException("Password must contain at least one lowercase letter");
        }
        if (!newPassword.matches(".*\\d.*")) {
            throw new PasswordPolicyViolationException("Password must contain at least one digit");
        }
        if (!newPassword.matches(".*[^a-zA-Z0-9].*")) {
            throw new PasswordPolicyViolationException("Password must contain at least one special character");
        }
        if (newPassword.toLowerCase().contains(username.toLowerCase())) {
            throw new PasswordPolicyViolationException("Password cannot contain your username");
        }
        if (newPassword.toLowerCase().contains(email.toLowerCase())) {
            throw new PasswordPolicyViolationException("Password cannot contain your email");
        }
    }
}
