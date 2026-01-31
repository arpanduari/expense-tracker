package dev.expensewise.backend.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 3)
    private String currency;

    private String publicId;
    private String secureUrl;

    private boolean isVerified;

    private LocalDate verifiedDate;

    private String oauthProvider;
    private String oauthProviderId;
}