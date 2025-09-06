package dev.arpan.expensetracker.config.jte;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

/**
 * @author arpan
 * @since 9/7/25
 */
@Configuration
public class JteConfig {
    @Bean
    public TemplateEngine templateEngine() {
        CodeResolver codeResolver = new ResourceCodeResolver("templates", getClass().getClassLoader());
        return TemplateEngine.create(codeResolver, Path.of("jte-classes"), ContentType.Html, getClass().getClassLoader());
    }
}
