package dev.arpan.expensetracker.repository;

import dev.arpan.expensetracker.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/5/25
 */
@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    void deleteByExpiryTimeBefore(LocalDateTime now);

    Optional<OtpVerification> findByToken(String token);

    Optional<OtpVerification> findByEmail(String email);
}
