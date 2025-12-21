package dev.arpan.expensetracker.auth;

import dev.arpan.expensetracker.auth.dto.*;
import dev.arpan.expensetracker.user.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author arpan
 * @since 8/3/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "Operations related to Authentication")
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;
    private final UserUtil userUtil;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided registration details. "
                    + "Returns the registered user information upon successful creation.",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "User registered successfully",
                        content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request."),
                @ApiResponse(responseCode = "409", description = "User with the same email/username already exists.")
            })
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        RegisterResponse response = authService.createUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user with their credentials. "
                    + "Returns an access accessToken and a refresh accessToken on successful authentication.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User logged in successfully",
                        content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                @ApiResponse(responseCode = "401", description = "Invalid username or password")
            })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/login/web")
    public ResponseEntity<WebLoginResponse> loginWeb(
            @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800L)
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new WebLoginResponse(loginResponse.accessToken(), loginResponse.username()));
    }

    @PostMapping("/refresh/web")
    public ResponseEntity<WebRefreshResponse> refreshWebToken(
            @CookieValue(value = "refreshToken", required = false) String refreshCookie, HttpServletResponse response) {
        if (refreshCookie == null || refreshCookie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new WebRefreshResponse(null));
        }
        RefreshResponse refreshResponse = authService.refreshToken(refreshCookie);
        if (refreshResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new WebRefreshResponse(null));
        }
        ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", refreshResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800L)
                .build();
        response.addHeader("Set-Cookie", newRefreshCookie.toString());
        return ResponseEntity.status(HttpStatus.OK).body(new WebRefreshResponse(refreshResponse.accessToken()));
    }

    @PostMapping("/login/mobile")
    public ResponseEntity<MobileLoginResponse> loginMobile(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MobileLoginResponse(
                        loginResponse.accessToken(), loginResponse.refreshToken(), loginResponse.username()));
    }

    @PostMapping("/refresh/mobile")
    public ResponseEntity<MobileRefreshResponse> refreshMobileToken(@RequestBody @Valid RefreshRequest refreshRequest) {
        RefreshResponse refreshResponse = authService.refreshToken(refreshRequest.refreshToken());
        return refreshResponse == null
                ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MobileRefreshResponse(null, null))
                : ResponseEntity.status(HttpStatus.OK)
                        .body(new MobileRefreshResponse(refreshResponse.accessToken(), refreshResponse.refreshToken()));
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify OTP",
            description = "Verifies the one-time password (OTP) for a given accessToken. "
                    + "Returns verification status and related details.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OTP verified successfully",
                        content = @Content(schema = @Schema(implementation = VerifyResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                @ApiResponse(responseCode = "401", description = "Invalid or expired OTP"),
                @ApiResponse(responseCode = "403", description = "OTP verification not allowed"),
                @ApiResponse(responseCode = "500", description = "Internal server error during verification")
            })
    public ResponseEntity<VerifyResponse> verifyOtp(@RequestBody @Valid OtpVerifyRequest otpVerifyRequest) {
        VerifyResponse verifyResponse = otpService.verifyOtp(otpVerifyRequest.token(), otpVerifyRequest.otp());
        return ResponseEntity.status(verifyResponse.status()).body(verifyResponse);
    }

    @PostMapping("/resend-otp")
    @Operation(
            summary = "Resend OTP",
            description =
                    "Resends a new OTP to the specified email address if the previous OTP expired or was not received.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OTP resent successfully",
                        content = @Content(schema = @Schema(implementation = OtpResendResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid email format or missing email parameter"),
                @ApiResponse(responseCode = "404", description = "User with the given email not found"),
                @ApiResponse(responseCode = "429", description = "Too many OTP resend attempts"),
                @ApiResponse(responseCode = "500", description = "Internal server error while resending OTP")
            })
    public ResponseEntity<OtpResendResponse> resendOtp(@RequestParam @NotNull @NotBlank String email) {
        OtpResendResponse otpResendResponse = otpService.resendOtp(email);
        return ResponseEntity.ok(otpResendResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse resetPasswordResponse = authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(resetPasswordResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse forgotPasswordResponse = authService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok(forgotPasswordResponse);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        ChangePasswordResponse changePasswordResponse = authService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok(changePasswordResponse);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        LoginResponse loginResponse = authService.googleLogin(googleLoginRequest.idToken());
        return ResponseEntity.ok(loginResponse);
    }
}
