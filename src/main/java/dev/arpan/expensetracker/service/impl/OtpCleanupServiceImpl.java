package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.repository.OtpVerificationRepository;
import dev.arpan.expensetracker.service.OtpCleanupService;
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
public class OtpCleanupServiceImpl implements OtpCleanupService {
    private final OtpVerificationRepository otpVerificationRepository;

    @Override
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cleanupExpiredOtp() {
        otpVerificationRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}
