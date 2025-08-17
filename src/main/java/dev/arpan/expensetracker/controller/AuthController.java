package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.service.AuthService;
import dev.arpan.expensetracker.service.OtpService;
import jakarta.validation.Valid;
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
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        RegisterResponse response = authService.createUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        RefreshResponseDTO refreshResponseDTO = authService.refreshToken(refreshRequest);
        if (refreshResponseDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RefreshResponseDTO(null, null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(refreshResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
    }


    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verifyOtp(@RequestBody OtpVerifyRequest otpVerifyRequest) {
        VerifyResponse verifyResponse = otpService.verifyOtp(otpVerifyRequest.getToken(), otpVerifyRequest.getOtp());
        return ResponseEntity.status(verifyResponse.status()).body(verifyResponse);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<OtpResendResponse> resendOtp(@RequestParam String email) {
        OtpResendResponse otpResendResponse = otpService.resendOtp(email);
        return ResponseEntity.ok(otpResendResponse);
    }

}
