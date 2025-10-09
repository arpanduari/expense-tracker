package dev.arpan.expensetracker.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arpan.expensetracker.auth.util.JWTUtil;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author arpan
 * @since 10/9/25
 */
@Service
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${app.frontend.path}")
    private String frontendPath;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User user = oAuth2User.getUser();

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

        Map<String, String> tokens = Map.of(
                "accessToken", accessToken
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7L * 24L * 3600L)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        new ObjectMapper().writeValue(response.getWriter(), tokens);
    }
}
