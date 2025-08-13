package dev.arpan.expensetracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 8/5/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String otp;
    @Column(unique = true, nullable = false, updatable = false)
    private String token;
    private LocalDateTime expiryTime;
    private boolean verified;
}
