package dev.expensewise.backend.auth;

import dev.expensewise.backend.auth.dto.*;
import dev.expensewise.backend.user.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(
        name = "Authentication Management",
        description = "APIs related to user authentication, authorization, OTP verification, and password management")
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
    @Operation(
            summary = "User Login for web",
            description = """
                 Authenticates user credentials for web applications.
                        - Access token is returned in response body
                        - Refresh token is stored in an HttpOnly cookie
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User logged in successfully",
                        content = @Content(schema = @Schema(implementation = WebLoginResponse.class)),
                        headers = {
                            @Header(
                                    name = "Set-Cookie",
                                    description = "HttpOnly refresh token cookie",
                                    schema = @Schema(implementation = String.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                @ApiResponse(responseCode = "401", description = "Invalid username or password")
            })
    public ResponseEntity<WebLoginResponse> loginWeb(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new WebLoginResponse(
                        loginResponse.accessToken(), loginResponse.refreshToken(), loginResponse.username()));
    }

    @PostMapping("/refresh/web")
    @Operation(
            summary = "Refresh access token (Web)",
            description = """
                Generates a new access token using refresh token
                stored in HttpOnly cookies.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Access token refreshed successfully",
                        content = @Content(schema = @Schema(implementation = WebRefreshResponse.class))),
                @ApiResponse(responseCode = "401", description = "Missing or invalid refresh token")
            })
    public ResponseEntity<WebRefreshResponse> refreshWebToken(@RequestBody @Valid RefreshRequest refreshRequest) {
        RefreshResponse refreshResponse = authService.refreshToken(refreshRequest.refreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                        .body(new WebRefreshResponse(refreshResponse.accessToken(), refreshResponse.refreshToken()));
    }

    @Operation(
            summary = "User login (Mobile)",
            description = """
                Authenticates user for mobile clients.
                Access and refresh tokens are returned in response body.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Login successful",
                        content = @Content(schema = @Schema(implementation = MobileLoginResponse.class)))
            })
    @PostMapping("/login/mobile")
    public ResponseEntity<MobileLoginResponse> loginMobile(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MobileLoginResponse(
                        loginResponse.accessToken(), loginResponse.refreshToken(), loginResponse.username()));
    }

    @PostMapping("/refresh/mobile")
    @Operation(
            summary = "Refresh access token (Mobile)",
            description = """
                Generates new access and refresh tokens using a valid refresh token.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Token refreshed successfully",
                        content = @Content(schema = @Schema(implementation = MobileRefreshResponse.class))),
                @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
            })
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
    @Operation(
            summary = "Reset password",
            description = """
                Resets user password using a valid reset token.
                Typically used after initiating the forgot password flow.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Password reset successfully",
                        content = @Content(schema = @Schema(implementation = ResetPasswordResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid reset token or password"),
                @ApiResponse(responseCode = "401", description = "Reset token expired or invalid")
            })
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse resetPasswordResponse = authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(resetPasswordResponse);
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password",
            description = """
                Initiates the password reset flow.
                Sends a password reset link or OTP to the registered email address.
                This endpoint is public.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Password reset instructions sent successfully",
                        content = @Content(schema = @Schema(implementation = ForgotPasswordResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid email format"),
                @ApiResponse(responseCode = "404", description = "User with given email not found")
            })
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse forgotPasswordResponse = authService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok(forgotPasswordResponse);
    }

    @PatchMapping("/change-password")
    @Operation(
            summary = "Change password",
            description = "Allows an authenticated user to change their password",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Password changed successfully",
                        content = @Content(schema = @Schema(implementation = ChangePasswordResponse.class)))
            })
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        ChangePasswordResponse changePasswordResponse = authService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok(changePasswordResponse);
    }

    @PostMapping("/google")
    @Operation(
            summary = "Login with Google",
            description = """
                Authenticates or registers a user using Google OAuth.
                Accepts a Google ID token obtained from the frontend.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Google login successful",
                        content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid Google ID token"),
                @ApiResponse(responseCode = "401", description = "Google authentication failed")
            })
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        LoginResponse loginResponse = authService.googleLogin(googleLoginRequest.idToken());
        return ResponseEntity.ok(loginResponse);
    }
}
