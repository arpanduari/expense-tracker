package dev.expensewise.backend.config.cloud;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author arpan
 * @since 8/30/25
 */
@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.secret.cloud-name}")
    private String cloudName;
    @Value("${cloudinary.secret.api-key}")
    private String apiKey;
    @Value("${cloudinary.secret.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> cloudinaryConfigMp = Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );
        return new Cloudinary(cloudinaryConfigMp);
    }
}
