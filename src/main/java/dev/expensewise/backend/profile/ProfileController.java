package dev.expensewise.backend.profile;

import dev.expensewise.backend.profile.dto.ProfilePictureDeleteResponse;
import dev.expensewise.backend.profile.dto.ProfilePictureUploadResponse;
import dev.expensewise.backend.config.security.CustomUserDetails;
import dev.expensewise.backend.user.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author arpan
 * @since 8/30/25
 */
@Tag(
        name = "Profile Management",
        description = "APIs related to user profile operations such as profile picture upload and deletion")
@RestController
@RequestMapping("${api.base}${api.version}/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileImageService profileImageService;
    private final UserUtil userUtil;

    @PostMapping("/profile-picture")
    @Operation(
            summary = "Upload profile picture",
            description = """
                Uploads or replaces the authenticated user's profile picture.
                Only image files are allowed.
                The previous profile picture (if any) will be replaced.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Profile picture uploaded successfully",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ProfilePictureUploadResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid file or unsupported file type"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "413", description = "File size exceeds allowed limit")
            })
    public ResponseEntity<ProfilePictureUploadResponse> uploadProfilePicture(
            @Parameter(
                            description = "Profile picture file (jpg, jpeg, png)",
                            required = true,
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    @RequestParam("file")
                    MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProfilePictureUploadResponse response = profileImageService.uploadProfileImage(userDetails.getUser(), file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile-picture")
    @Operation(
            summary = "Delete profile picture",
            description = """
                Deletes the authenticated user's profile picture.
                If no profile picture exists, operation is idempotent.
                """,
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Profile picture deleted successfully",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ProfilePictureDeleteResponse.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Profile picture not found")
            })
    public ResponseEntity<ProfilePictureDeleteResponse> deleteProfilePicture(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProfilePictureDeleteResponse profilePictureDeleteResponse =
                profileImageService.deleteProfileImage(userDetails.getUser());
        return ResponseEntity.ok(profilePictureDeleteResponse);
    }
}
