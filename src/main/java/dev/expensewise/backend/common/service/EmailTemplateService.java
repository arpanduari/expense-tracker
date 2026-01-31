package dev.expensewise.backend.common.service;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author arpan
 * @since 8/5/25
 */
@Service
public class EmailTemplateService {
    @Value("${app.logo}")
    private String logoUrl;
    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getEmailContent(String fileName, Map<String, String> data) {
        StringOutput output = new StringOutput();
        Map<String, String> defaultData = new HashMap<>();
        defaultData.put("appLogo", logoUrl);
        defaultData.put("year", LocalDate.now().getYear() + "");
        defaultData.putAll(data);
        templateEngine.render(fileName, defaultData, output);
        return output.toString();

    }
}
