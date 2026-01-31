package dev.expensewise.backend.profile;

import com.github.atomfrede.jadenticon.Jadenticon;
import dev.expensewise.backend.cloud.dto.CloudinaryUploadResponse;
import dev.expensewise.backend.cloud.service.CloudinaryService;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.profile.dto.ProfilePictureDeleteResponse;
import dev.expensewise.backend.profile.dto.ProfilePictureUploadResponse;
import dev.expensewise.backend.profile.util.InMemoryMultipartFile;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.UserRepository;
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
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        MultipartFile profileImage = createProfileImage(username);
        CloudinaryUploadResponse cloudinaryUploadResponse =
                cloudinaryService.uploadFile(profileImage, "profile-images", user.getPublicId());

        user.setPublicId(cloudinaryUploadResponse.publicId());
        user.setSecureUrl(cloudinaryUploadResponse.url());

        userRepository.save(user);
    }

    @Transactional
    public ProfilePictureUploadResponse uploadProfileImage(User user, MultipartFile profileImage) {
        User existingUser = userRepository
                .findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId() + ""));
        CloudinaryUploadResponse cloudinaryUploadResponse =
                cloudinaryService.uploadFile(profileImage, "profile-images", existingUser.getPublicId());

        existingUser.setPublicId(cloudinaryUploadResponse.publicId());
        existingUser.setSecureUrl(cloudinaryUploadResponse.url());

        userRepository.save(existingUser);

        return new ProfilePictureUploadResponse(existingUser.getSecureUrl(), "Profile picture uploaded successfully");
    }

    public ProfilePictureDeleteResponse deleteProfileImage(User user) {
        User existingUser = userRepository
                .findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId() + ""));
        cloudinaryService.deleteFile(existingUser.getUsername());
        return new ProfilePictureDeleteResponse(true, "Profile picture deleted successfully");
    }

    private MultipartFile createProfileImage(String username) {
        try {
            String fileName = username.toLowerCase() + "-avatar";
            File pngImage = Jadenticon.from(username).withSize(400).png(fileName);
            byte[] pngBytes = FileUtils.readFileToByteArray(pngImage);
            return new InMemoryMultipartFile(pngBytes, "avatar", fileName, "image/png");
        } catch (IOException | TranscoderException e) {
            throw new RuntimeException("Failed to create profile image.");
        }
    }
}
