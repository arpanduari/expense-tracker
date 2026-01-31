package dev.expensewise.backend.common.service;

import brevo.ApiClient;
import brevoApi.TransactionalEmailsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author arpan
 * @since 12/22/25
 */
@Configuration
@Profile("prod")
public class BrevoConfig {
    @Value("${app.config.mail.apiKey}")
    private String brevoApiKey;

    @Bean
    public TransactionalEmailsApi transactionalEmailsApi() {
        ApiClient apiClient = brevo.Configuration.getDefaultApiClient();
        apiClient.setApiKey(brevoApiKey);

        return new TransactionalEmailsApi(apiClient);
    }
}
