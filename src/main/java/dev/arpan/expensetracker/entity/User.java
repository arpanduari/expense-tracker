package dev.arpan.expensetracker.entity;

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
    @Column(nullable = false, columnDefinition = "varchar(3) default 'INR'")
    private String currency = "INR";
    private boolean isVerified;
    private LocalDate verifiedDate;
}
