package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.OtpResendResponse;
import dev.arpan.expensetracker.dto.OtpVerifyRequest;
import dev.arpan.expensetracker.dto.VerifyResponse;
import dev.arpan.expensetracker.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author arpan
 * @since 8/5/25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base}${api.version}/otp")
public class OtpController {
    private final OtpService otpService;

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
