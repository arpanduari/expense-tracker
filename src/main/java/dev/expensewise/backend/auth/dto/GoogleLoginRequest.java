package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for Google OAuth2 login")
public record GoogleLoginRequest(
        @Schema(description = "Google ID accessToken received from client", example = "eyJhbGciOiJSUzI1NiIs...")
        String idToken
) { }
