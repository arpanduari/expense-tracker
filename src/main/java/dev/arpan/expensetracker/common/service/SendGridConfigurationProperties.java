package dev.arpan.expensetracker.common.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

/**
 * @author arpan
 * @since 10/6/25
 */
@ConfigurationProperties(prefix = "app.config.mail")
@Profile("prod")
@Getter
@Setter
public class SendGridConfigurationProperties {
    private String fromEmail;
    private String fromName;
    private String apiKey;
}
