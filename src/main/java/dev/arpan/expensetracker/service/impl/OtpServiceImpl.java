package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.dto.OtpResendResponse;
import dev.arpan.expensetracker.dto.VerifyResponse;
import dev.arpan.expensetracker.entity.OtpVerification;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.messaging.AccountCreatedMessageProducer;
import dev.arpan.expensetracker.messaging.OtpProducer;
import dev.arpan.expensetracker.repository.OtpVerificationRepository;
import dev.arpan.expensetracker.repository.UserRepository;
import dev.arpan.expensetracker.service.OtpService;
import dev.arpan.expensetracker.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;
    private final OtpProducer otpProducer;
    private final AccountCreatedMessageProducer accountCreatedMessageProducer;
    @Value("${app.frontend.path}")
    private String frontendPath;

    @Override
    public void sendOtp(String toEmail, String otp, String username) {
        otpProducer.sendOtpMessage(toEmail, otp, username);
    }

    @Override
    public VerifyResponse verifyOtp(String token, String otp) {
        OtpVerification otpVerification = otpVerificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
            return new VerifyResponse("OTP has expired. Please resend OTP", HttpStatus.GONE);
        }
        if (!otpVerification.getOtp().equals(otp)) {
            return new VerifyResponse("Invalid OTP", HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findByUsernameOrEmail(otpVerification.getEmail(), otpVerification.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));
        user.setVerified(true);
        user.setVerifiedDate(LocalDate.now());
        userRepository.save(user);

        String loginUrl = frontendPath + "/login";

        accountCreatedMessageProducer.sendAccountCreatedMessage(user.getEmail(), user.getUsername(), loginUrl);

        otpVerificationRepository.delete(otpVerification);
        return new VerifyResponse("OTP verified successfully", HttpStatus.OK);

    }

    @Override
    public OtpResendResponse resendOtp(String toEmail) {
        otpVerificationRepository.findByEmail(toEmail)
                .orElseThrow(() -> new RuntimeException("Invalid email"));
        OtpVerification otpVerification = OtpUtil.createOtpVerification(toEmail);
        otpVerificationRepository.save(otpVerification);
        User user = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", toEmail));
        sendOtp(toEmail, otpVerification.getOtp(), user.getUsername());
        return OtpResendResponse.builder()
                .message("OTP Resend successfully. Please check your email for verification.")
                .verificationUrl("/verify-otp?token=" + otpVerification.getToken())
                .build();

    }

}
