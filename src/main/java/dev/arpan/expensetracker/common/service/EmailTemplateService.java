package dev.arpan.expensetracker.common.service;

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

    /*
    public String getEmailContent(String fileName, Map<String, String> data) throws IOException {
        Path templatePath = new ClassPathResource("templates/" + fileName)
                .getFile()
                .toPath();
        String htmlContent = Files.readString(templatePath);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            htmlContent = htmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        htmlContent = htmlContent.replace("${APP_LOGO}", logoUrl);
        htmlContent = htmlContent.replace("${YEAR}", LocalDate.now().getYear() + "");
        return htmlContent;
    }
    */
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
