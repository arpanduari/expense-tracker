package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.constants.ApplicationConstants;
import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.entity.OtpVerification;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.mapper.UserMapper;
import dev.arpan.expensetracker.repository.OtpVerificationRepository;
import dev.arpan.expensetracker.repository.UserRepository;
import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.service.OtpService;
import dev.arpan.expensetracker.service.UserService;
import dev.arpan.expensetracker.utils.JWTUtil;
import dev.arpan.expensetracker.utils.OtpUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author arpan
 * @since 8/3/25
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final OtpVerificationRepository otpVerificationRepository;

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
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login details: " + username));
        return UserMapper.toUserDto(user);
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
}
