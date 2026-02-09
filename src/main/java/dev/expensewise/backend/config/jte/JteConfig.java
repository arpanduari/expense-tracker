package dev.expensewise.backend.config.jte;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.springframework.boot.autoconfigure.JteProperties;
import gg.jte.springframework.boot.autoconfigure.JteViewResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.nio.file.Path;

@Configuration
public class JteConfig {

    @Value("${jte.development-mode:false}")
    private boolean developmentMode;

    @Bean
    public TemplateEngine jteTemplateEngine() {
        if (developmentMode) {
            DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src/main/resources/templates"));
            return TemplateEngine.create(codeResolver, ContentType.Html);
        } else {
            return TemplateEngine.createPrecompiled(
                    null, ContentType.Html, getClass().getClassLoader(), "gg.jte.generated.precompiled");
        }
    }

    @Bean
    public JteViewResolver jteViewResolver(TemplateEngine jteTemplateEngine) {
        JteViewResolver resolver = new JteViewResolver(jteTemplateEngine, new JteProperties());
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }
}
