package dev.expensewise.backend.auth;

import dev.expensewise.backend.auth.dto.OtpResendResponse;
import dev.expensewise.backend.auth.dto.VerifyResponse;
import dev.expensewise.backend.auth.util.OtpUtil;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.messaging.account.AccountCreatedMessageProducer;
import dev.expensewise.backend.messaging.auth.OtpProducer;
import dev.expensewise.backend.messaging.profile.ProfileImageEventProducer;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.UserRepository;
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
public class OtpService {
    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;
    private final OtpProducer otpProducer;
    private final AccountCreatedMessageProducer accountCreatedMessageProducer;
    private final ProfileImageEventProducer profileImageEventProducer;

    @Value("${app.frontend.path}")
    private String frontendPath;


    public void sendOtp(String toEmail, String otp, String username) {
        otpProducer.sendOtpMessage(toEmail, otp, username);
    }


    public VerifyResponse verifyOtp(String token, String otp) {
        OtpVerification otpVerification = otpVerificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid accessToken"));
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

        profileImageEventProducer.sendProfileImageEvent(user.getUsername());

        String loginUrl = frontendPath + "/login";

        accountCreatedMessageProducer.sendAccountCreatedMessage(user.getEmail(), user.getUsername(), loginUrl);

        otpVerificationRepository.delete(otpVerification);
        return new VerifyResponse("OTP verified successfully", HttpStatus.OK);

    }


    public OtpResendResponse resendOtp(String toEmail) {
        otpVerificationRepository.findByEmail(toEmail)
                .orElseThrow(() -> new RuntimeException("Invalid email"));
        OtpVerification otpVerification = OtpUtil.createOtpVerification(toEmail);
        otpVerificationRepository.save(otpVerification);
        User user = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", toEmail));
        sendOtp(toEmail, otpVerification.getOtp(), user.getUsername());
        return new OtpResendResponse("OTP Resend successfully. Please check your email for verification", otpVerification.getToken());
    }

}
