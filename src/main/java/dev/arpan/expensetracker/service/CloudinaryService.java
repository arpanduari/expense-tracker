package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.CloudinaryUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author arpan
 * @since 8/30/25
 */
public interface CloudinaryService {
    CloudinaryUploadResponse uploadFile(MultipartFile file, String folderName, String oldPublicId);

    void deleteFile(String username);
}
