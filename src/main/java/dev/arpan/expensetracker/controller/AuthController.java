package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.service.AuthService;
import dev.arpan.expensetracker.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided registration details. " +
                    "Returns the registered user information upon successful creation.",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = RegisterResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request."
                    ),
                    @ApiResponse(
                            responseCode = "409", description = "User with the same email/username already exists."
                    )
            }
    )
    public ResponseEntity<RegisterResponse> register(
            @RequestBody
            @Valid
            RegisterRequestDTO registerRequestDTO
    ) {
        RegisterResponse response = authService.createUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token (and optionally a new refresh token) " +
                    "using a valid refresh token. If the refresh token is invalid or expired, " +
                    "an unauthorized error is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Token refreshed successfully",
                            content = @Content(schema = @Schema(implementation = RefreshResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                    @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
            }
    )
    public ResponseEntity<RefreshResponseDTO> refreshToken(@RequestBody @Valid RefreshRequest refreshRequest) {
        RefreshResponseDTO refreshResponseDTO = authService.refreshToken(refreshRequest);
        if (refreshResponseDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RefreshResponseDTO(null, null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(refreshResponseDTO);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user with their credentials. " +
                    "Returns an access token and a refresh token on successful authentication.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "User logged in successfully",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                    @ApiResponse(responseCode = "401", description = "Invalid username or password")
            }
    )
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify OTP",
            description = "Verifies the one-time password (OTP) for a given token. " +
                    "Returns verification status and related details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OTP verified successfully",
                            content = @Content(schema = @Schema(implementation = VerifyResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                    @ApiResponse(responseCode = "401", description = "Invalid or expired OTP"),
                    @ApiResponse(responseCode = "403", description = "OTP verification not allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal server error during verification")
            }
    )
    public ResponseEntity<VerifyResponse> verifyOtp(@RequestBody @Valid OtpVerifyRequest otpVerifyRequest) {
        VerifyResponse verifyResponse = otpService.verifyOtp(otpVerifyRequest.getToken(), otpVerifyRequest.getOtp());
        return ResponseEntity.status(verifyResponse.status()).body(verifyResponse);
    }

    @PostMapping("/resend-otp")
    @Operation(
            summary = "Resend OTP",
            description = "Resends a new OTP to the specified email address if the previous OTP expired or was not received.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP resent successfully",
                            content = @Content(schema = @Schema(implementation = OtpResendResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid email format or missing email parameter"),
                    @ApiResponse(responseCode = "404", description = "User with the given email not found"),
                    @ApiResponse(responseCode = "429", description = "Too many OTP resend attempts"),
                    @ApiResponse(responseCode = "500", description = "Internal server error while resending OTP")
            }
    )
    public ResponseEntity<OtpResendResponse> resendOtp(@RequestParam @NotNull @NotBlank String email) {
        OtpResendResponse otpResendResponse = otpService.resendOtp(email);
        return ResponseEntity.ok(otpResendResponse);
    }

}
