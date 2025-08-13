package dev.arpan.expensetracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author arpan
 * @since 8/4/25
 */
@Configuration
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiProperties {
    private String base;
    private String version;
    public String getFullPath(){
        return base + version;
    }
}
