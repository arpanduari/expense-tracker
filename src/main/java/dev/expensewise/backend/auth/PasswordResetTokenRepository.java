package dev.expensewise.backend.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/20/25
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUserIdAndExpiryTimeAfter(Long userId, LocalDateTime now);

    Optional<PasswordResetToken> findByUserId(Long userId);
}
