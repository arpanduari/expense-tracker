package dev.arpan.expensetracker.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;

/**
 * @author arpan
 * @since 8/5/25
 */
public final class EmailTemplateUtil {
    private EmailTemplateUtil() {
    }

    public static String getOtpEmailContent(String otp) throws IOException {
        Path templatePath = new ClassPathResource("templates/otp-page.html").getFile().toPath();
        String htmlContent = Files.readString(templatePath);
        htmlContent = htmlContent.replace("${OTP}", otp);
        htmlContent = htmlContent.replace("${YEAR}", String.valueOf(Year.now().getValue()));
        return htmlContent;
    }
}
