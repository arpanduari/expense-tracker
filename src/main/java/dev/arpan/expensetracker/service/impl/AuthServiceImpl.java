package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.constants.ApplicationConstants;
import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.entity.OtpVerification;
import dev.arpan.expensetracker.entity.PasswordResetToken;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.exception.PasswordResetTokenAlreadySentException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.mapper.UserMapper;
import dev.arpan.expensetracker.messaging.ForgotPasswordMessageProducer;
import dev.arpan.expensetracker.repository.OtpVerificationRepository;
import dev.arpan.expensetracker.repository.PasswordResetTokenRepository;
import dev.arpan.expensetracker.repository.UserRepository;
import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.service.AuthService;
import dev.arpan.expensetracker.service.OtpService;
import dev.arpan.expensetracker.utils.ForgotPasswordUtil;
import dev.arpan.expensetracker.utils.JWTUtil;
import dev.arpan.expensetracker.utils.OtpUtil;
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
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final ForgotPasswordUtil forgotPasswordUtil;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ForgotPasswordMessageProducer forgotPasswordMessageProducer;

    @Value("${app.frontend.path}")
    private String frontendPath;

    @Override
    public RegisterResponse createUser(RegisterRequestDTO registerRequestDTO) {
        User user = UserMapper.toUser(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        OtpVerification otpVerification = OtpUtil.createOtpVerification(savedUser.getEmail());
        otpVerificationRepository.save(otpVerification);
        otpService.sendOtp(registerRequestDTO.getEmail(), otpVerification.getOtp());
        return RegisterResponse.builder()
                .message("User registered successfully. Please check your email for verification.")
                .verificationUrl("/verify?token=" + otpVerification.getToken())
                .build();
    }


    @Override
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

    @Override
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

    @Override
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

        forgotPasswordMessageProducer.sendForgotPasswordMessage(user.getEmail(), link);

        return ForgotPasswordResponse.builder()
                .message("Password reset link sent to your email")
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(resetPasswordRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Password reset token", "id", resetPasswordRequest.getId() + ""));
        if (!passwordEncoder.matches(resetPasswordRequest.getToken(), passwordResetToken.getTokenHash())) {
            throw new ResourceNotFoundException("Password reset token", "id", resetPasswordRequest.getId() + "");
        }
        User user = userRepository.findById(passwordResetToken.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", passwordResetToken.getUser().getId() + ""));
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return ResetPasswordResponse.builder()
                .message("Password reset successful")
                .build();
    }
}
