package dev.arpan.expensetracker.profile;

import dev.arpan.expensetracker.profile.dto.ProfilePictureDeleteResponse;
import dev.arpan.expensetracker.profile.dto.ProfilePictureUploadResponse;
import dev.arpan.expensetracker.user.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author arpan
 * @since 8/30/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileImageService profileImageService;
    private final UserUtil userUtil;
    
    @PostMapping("/profile-picture")
    public ResponseEntity<ProfilePictureUploadResponse> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        ProfilePictureUploadResponse response = profileImageService.uploadProfileImage(userId, file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<ProfilePictureDeleteResponse> deleteProfilePicture(Authentication authentication){
        Long userId = userUtil.getUserId(authentication);
        ProfilePictureDeleteResponse profilePictureDeleteResponse = profileImageService.deleteProfileImage(userId);
        return ResponseEntity.ok(profilePictureDeleteResponse);
    }
}
