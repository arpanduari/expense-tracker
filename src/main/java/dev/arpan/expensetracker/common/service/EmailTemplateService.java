package dev.arpan.expensetracker.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

/**
 * @author arpan
 * @since 8/5/25
 */
@Component
public class EmailTemplateService {
    @Value("${app.logo}")
    private String logoUrl;

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
}
