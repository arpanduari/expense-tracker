package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.ProfilePictureDeleteResponse;
import dev.arpan.expensetracker.dto.ProfilePictureUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author arpan
 * @since 8/30/25
 */
public interface ProfileImageService {
    void addDefaultProfileImage(String username);
    ProfilePictureUploadResponse uploadProfileImage(Long userId, MultipartFile profileImage);
    ProfilePictureDeleteResponse deleteProfileImage(Long userId);
}
