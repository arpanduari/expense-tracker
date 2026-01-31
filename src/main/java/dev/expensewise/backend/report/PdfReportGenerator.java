package dev.expensewise.backend.report;

import com.itextpdf.html2pdf.HtmlConverter;
import dev.expensewise.backend.constants.file.FileNameConstants;
import dev.expensewise.backend.expense.Expense;
import dev.expensewise.backend.user.User;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author arpan
 * @since 9/10/25
 */
@Component
public class PdfReportGenerator implements ReportGenerator {
    private final TemplateEngine templateEngine;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private static final String TRANSACTION_FOLDER = "transactions/"
    @Value("${app.static-logo}")
    private String appLogo;

    public PdfReportGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] generate(User user, LocalDate startDate, LocalDate endDate, List<Expense> expenses) {
        StringOutput output = new StringOutput();
        byte[] logoBytes;
        try {
            ClassPathResource logoResource = new ClassPathResource("static/images/" + appLogo);
            logoBytes = logoResource.getInputStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading logo file.");
        }
        String base64Logo = Base64.getEncoder().encodeToString(logoBytes);
        String logoDataUri = "data:image/png;base64," + base64Logo;


        Map<String, Object> data = Map.of("logoUrl", logoDataUri,
                "username", user.getUsername(),
                "email", user.getEmail(),
                "startDate", startDate.format(dateTimeFormatter),
                "endDate", endDate.format(dateTimeFormatter),
                "expenses", expenses,
                "year", startDate.getYear() + ""
        );
        templateEngine.render("transactions-template.jte", data, output);
        String htmlContent = output.toString();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public String getExtension() {
        return FileNameConstants.REPORT_FILE_EXTENSION_PDF;
    }

    @Override
    public String getMediaType() {
        return MediaType.APPLICATION_PDF_VALUE;
    }

    @Override
    public ReportType getType() {
        return ReportType.PDF;
    }
}
