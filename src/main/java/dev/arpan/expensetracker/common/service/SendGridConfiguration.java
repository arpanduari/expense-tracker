package dev.arpan.expensetracker.common.service;

import com.sendgrid.SendGrid;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author arpan
 * @since 10/6/25
 */
@Configuration
@EnableConfigurationProperties(SendGridConfigurationProperties.class)
public class SendGridConfiguration {
    private final SendGridConfigurationProperties sendGridConfigurationProperties;

    public SendGridConfiguration(SendGridConfigurationProperties sendGridConfigurationProperties) {
        this.sendGridConfigurationProperties = sendGridConfigurationProperties;
    }

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendGridConfigurationProperties.getApiKey());
    }
}
