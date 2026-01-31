package dev.expensewise.backend.cloud.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dev.expensewise.backend.cloud.dto.CloudinaryUploadResponse;
import dev.expensewise.backend.messaging.profile.ProfileImageEventProducer;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author arpan
 * @since 8/30/25
 */
@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final ProfileImageEventProducer profileImageEventProducer;

    
    public CloudinaryUploadResponse uploadFile(MultipartFile file, String folderName, String oldPublicId) {
        try {
            if (oldPublicId != null && !oldPublicId.isBlank()) {
                cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());
            }

            Map<String, Object> options = Map.of("folder", folderName);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            String publicId = (String) uploadResult.get("public_id");
            String url = (String) uploadResult.get("secure_url");
            return new CloudinaryUploadResponse(publicId, url);
        } catch (IOException ioException) {
            throw new RuntimeException("Failed to upload file.");
        }
    }

    
    public void deleteFile(String username) {
        try {
            User user = userRepository.findByUsernameOrEmail(username, username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
            cloudinary.uploader().destroy(user.getPublicId(), ObjectUtils.emptyMap());
            profileImageEventProducer.sendProfileImageEvent(username);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete file.");
        }
    }
}
