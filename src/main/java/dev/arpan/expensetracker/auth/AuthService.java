package dev.arpan.expensetracker.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import dev.arpan.expensetracker.auth.dto.*;
import dev.arpan.expensetracker.auth.util.ForgotPasswordUtil;
import dev.arpan.expensetracker.auth.util.JWTUtil;
import dev.arpan.expensetracker.auth.util.OtpUtil;
import dev.arpan.expensetracker.common.mapper.UserMapper;
import dev.arpan.expensetracker.config.security.GoogleVerifierService;
import dev.arpan.expensetracker.constants.application.ApplicationConstants;
import dev.arpan.expensetracker.exception.*;
import dev.arpan.expensetracker.messaging.account.ChangePasswordMessageProducer;
import dev.arpan.expensetracker.messaging.account.ResetSuccessMessageProducer;
import dev.arpan.expensetracker.messaging.auth.ForgotPasswordMessageProducer;
import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
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
    private final MessageSource messageSource;
    private final GoogleVerifierService googleVerifier;

    @Value("${app.frontend.path}")
    private String frontendPath;

    public RegisterResponse createUser(RegisterRequest registerRequest) {

        if (userRepository.existsUserByEmail(registerRequest.email())) {
            throw new EmailAlreadyExistsException("User already exists with email: " + registerRequest.email());
        }
        if (userRepository.existsUserByUsername(registerRequest.username())) {
            throw new UsernameAlreadyExistsException("Username already exists with username: " + registerRequest.username());
        }

        User user = UserMapper.toUser(registerRequest);
        isValidPassword(user.getUsername(), user.getEmail(), registerRequest.password());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        OtpVerification otpVerification = OtpUtil.createOtpVerification(savedUser.getEmail());
        otpVerificationRepository.save(otpVerification);
        otpService.sendOtp(registerRequest.email(), otpVerification.getOtp(), savedUser.getUsername());
        return new RegisterResponse(messageSource.getMessage("user.register.success", null, getLocale()),
                otpVerification.getToken());
    }


    public RefreshResponse refreshToken(String refreshToken) {
        try {
            Claims claims = jwtUtil.parseToken(refreshToken);
            if (jwtUtil.isTokenExpired(claims.getExpiration())) {
                return null;
            }
            String username = claims.get("username", String.class);
            Long userId = claims.get(ApplicationConstants.USER_ID, Long.class);
            String newAccessToken = jwtUtil.generateAccessToken(username, userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(username, userId);
            return new RefreshResponse(newAccessToken, newRefreshToken);
        } catch (Exception ex) {
            return null;
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.userIdentifier(),
                        loginRequest.password())
        );
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        String accessToken = jwtUtil.generateAccessToken(authentication.getName(), userId);
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName(), userId);
        return new LoginResponse(accessToken, refreshToken, authentication.getName());
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email",
                        forgotPasswordRequest.email()));

        passwordResetTokenRepository.findByUserIdAndExpiryTimeAfter(user.getId(), LocalDateTime.now())
                .ifPresent(token -> {
                            Duration duration = Duration.between(LocalDateTime.now(), token.getExpiryTime());
                            throw new PasswordResetTokenAlreadySentException(
                                    messageSource.getMessage("forgot.password.already.sent", new Object[]{duration.toMinutes()}, getLocale()
                                    ));
                        }
                );

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

        String link = frontendPath + "/reset-password?accessToken=" + uuid + "&id=" + passwordResetToken.getId();

        forgotPasswordMessageProducer.sendForgotPasswordMessage(user.getEmail(), link, user.getUsername());

        return new ForgotPasswordResponse(messageSource.getMessage("forgot.password.link.sent", null, getLocale()));
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(resetPasswordRequest.id())
                .orElseThrow(() -> new ResourceNotFoundException("Password reset accessToken", "id", resetPasswordRequest.id() + ""));
        if (!passwordEncoder.matches(resetPasswordRequest.token(), passwordResetToken.getTokenHash())) {
            throw new ResourceNotFoundException("Password reset accessToken", "id", resetPasswordRequest.id() + "");
        }
        User user = userRepository.findById(passwordResetToken.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", passwordResetToken.getUser().getId() + ""));
        isValidPassword(user.getUsername(), user.getEmail(), resetPasswordRequest.newPassword());
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(user);

        String loginPath = frontendPath + "/login";
        resetSuccessMessageProducer.sendResetSuccessMessage(user.getEmail(), loginPath, user.getUsername());

        passwordResetTokenRepository.delete(passwordResetToken);
        return new ResetPasswordResponse(messageSource.getMessage("reset.password.success", null, getLocale()));
    }

    public ChangePasswordResponse changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId + ""));

        isValidPassword(user.getUsername(), user.getEmail(), changePasswordRequest.newPassword());

        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            throw new PasswordNotMatchingException(messageSource.getMessage("change.password.old.mismatch",
                    null, getLocale()));
        }

        if (passwordEncoder.matches(changePasswordRequest.newPassword(), user.getPassword())) {
            throw new PasswordNotMatchingException(messageSource.getMessage("change.password.same.as.old",
                    null, getLocale()));
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);

        String loginPath = frontendPath + "/login";
        changePasswordMessageProducer.sendChangePasswordMessage(user.getEmail(), loginPath);

        return new ChangePasswordResponse(true, messageSource.getMessage("change.password.success", null, getLocale()));
    }

    public void isValidPassword(String username, String email, String newPassword) {
        if (newPassword.length() < 8) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.min.length", null, getLocale()));
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.uppercase", null, getLocale()));
        }
        if (!newPassword.matches(".*[a-z].*")) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.lowercase", null, getLocale()));
        }
        if (!newPassword.matches(".*\\d.*")) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.digit", null, getLocale()));
        }
        if (!newPassword.matches(".*[^a-zA-Z0-9].*")) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.special", null, getLocale()));
        }
        if (newPassword.toLowerCase().contains(username.toLowerCase())) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.contains.username", null, getLocale()));
        }
        if (newPassword.toLowerCase().contains(email.toLowerCase())) {
            throw new PasswordPolicyViolationException(messageSource.getMessage("password.contains.email", null, getLocale()));
        }
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public LoginResponse googleLogin(String token) {
        GoogleIdToken.Payload payload = googleVerifier.verifyGoogleIdToken(token);

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");
        String sub = payload.getSubject();

        User user = createUserFromGoogle(email, name, picture, sub);

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId());

        return new LoginResponse(accessToken, refreshToken, user.getUsername());
    }

    private User createUserFromGoogle(String email, String name, String pictureUrl, String providerId) {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .username(name != null ? name : email.split("@")[0])
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .publicId(null)
                    .secureUrl(pictureUrl)
                    .isVerified(true)
                    .verifiedDate(LocalDate.now())
                    .oauthProvider("GOOGLE")
                    .oauthProviderId(providerId)
                    .build();
        } else {
            boolean updated = false;
            if (pictureUrl != null && !pictureUrl.equals(user.getSecureUrl())) {
                user.setSecureUrl(pictureUrl);
                updated = true;
            }
            if (user.getOauthProviderId() == null || !providerId.equals(user.getOauthProviderId())) {
                user.setOauthProvider("GOOGLE");
                user.setOauthProviderId(providerId);
                updated = true;
            }
            if (!user.isVerified()) {
                user.setVerified(true);
                user.setVerifiedDate(LocalDate.now());
                updated = true;
            }
            if (updated) {
                userRepository.save(user);
            }
        }
        return user;
    }
}
