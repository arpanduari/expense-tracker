package dev.arpan.expensetracker.service;

import java.io.IOException;
import java.util.Map;

/**
 * @author arpan
 * @since 8/21/25
 */
public interface EmailTemplateService {
    String getEmailContent(String fileName, Map<String, String> data) throws IOException;
}
