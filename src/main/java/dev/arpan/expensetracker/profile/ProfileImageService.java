package dev.arpan.expensetracker.profile;

import com.github.atomfrede.jadenticon.Jadenticon;
import dev.arpan.expensetracker.cloud.service.CloudinaryService;
import dev.arpan.expensetracker.cloud.dto.CloudinaryUploadResponse;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.profile.dto.ProfilePictureDeleteResponse;
import dev.arpan.expensetracker.profile.dto.ProfilePictureUploadResponse;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.UserRepository;
import dev.arpan.expensetracker.profile.util.InMemoryMultipartFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author arpan
 * @since 8/30/25
 */
@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;


    @Transactional
    public void addDefaultProfileImage(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        MultipartFile profileImage = createProfileImage(username);
        CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryService
                .uploadFile(profileImage, "profile-images", user.getPublicId());

        user.setPublicId(cloudinaryUploadResponse.publicId());
        user.setSecureUrl(cloudinaryUploadResponse.url());

        userRepository.save(user);
    }


    @Transactional
    public ProfilePictureUploadResponse uploadProfileImage(Long userId, MultipartFile profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId + ""));
        CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryService.uploadFile(profileImage,
                "profile-images", user.getPublicId());

        user.setPublicId(cloudinaryUploadResponse.publicId());
        user.setSecureUrl(cloudinaryUploadResponse.url());

        userRepository.save(user);

        return ProfilePictureUploadResponse.builder()
                .message("Profile picture uploaded successfully")
                .profilePictureUrl(user.getSecureUrl())
                .build();
    }


    public ProfilePictureDeleteResponse deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId + ""));
        cloudinaryService.deleteFile(user.getUsername());
        return ProfilePictureDeleteResponse.builder()
                .deleted(true)
                .message("Profile picture deleted successfully")
                .build();
    }


    private MultipartFile createProfileImage(String username) {
        try {
            String fileName = username.toLowerCase() + "-avatar";
            File pngImage = Jadenticon.from(username).withSize(400)
                    .png(fileName);
            byte[] pngBytes = FileUtils.readFileToByteArray(pngImage);
            return new InMemoryMultipartFile(pngBytes, "avatar", fileName, "image/png");
        } catch (IOException | TranscoderException e) {
            throw new RuntimeException("Failed to create profile image.");
        }
    }

}
