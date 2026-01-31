package dev.expensewise.backend.config.jte;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author arpan
 * @since 9/7/25
 */
@Configuration
public class JteConfig {
    @Bean
    public TemplateEngine templateEngine() {
        boolean isDev = isDevelopmentEnvironment();

        if (isDev) {
            ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", getClass().getClassLoader());
            return TemplateEngine.create(codeResolver, ContentType.Html);
        } else {
            return TemplateEngine.createPrecompiled(ContentType.Html);
        }
    }

    private boolean isDevelopmentEnvironment() {
        String env = System.getenv("SPRING_PROFILES_ACTIVE");
        return env == null || env.equalsIgnoreCase("dev");
    }
}
