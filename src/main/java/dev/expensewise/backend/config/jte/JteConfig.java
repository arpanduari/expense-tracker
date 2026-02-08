package dev.expensewise.backend.config.jte;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import gg.jte.springframework.boot.autoconfigure.JteViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class JteConfig {

    @Bean
    public TemplateEngine jteTemplateEngine() {
        ResourceCodeResolver codeResolver =
                new ResourceCodeResolver("templates", getClass().getClassLoader());
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    @Bean
    public JteViewResolver jteViewResolver(TemplateEngine jteTemplateEngine) {
        JteViewResolver resolver = new JteViewResolver(jteTemplateEngine, ".jte");
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }
}
