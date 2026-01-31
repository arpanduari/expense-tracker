package dev.expensewise.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
@RequiredArgsConstructor
public class OtpCleanupService {
    private final OtpVerificationRepository otpVerificationRepository;

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cleanupExpiredOtp() {
        otpVerificationRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}
