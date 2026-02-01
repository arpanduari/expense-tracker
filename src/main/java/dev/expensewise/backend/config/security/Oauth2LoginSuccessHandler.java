package dev.expensewise.backend.config.security;

import dev.expensewise.backend.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author arpan
 * @since 10/9/25
 */
@Service
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTService jwtService;
    public static final String ANDROID = "android";

    @Value("${app.frontend.path}")
    private String frontendPath;

    @Value("${app.android.deeplink.scheme:expensewise}")
    private String androidDeepLinkScheme;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User user = oAuth2User.getUser();

        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getId());
        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getId());

        ClientType clientType = detectClientType(request);
        if (clientType.equals(ClientType.WEB)) {
            handleWebSuccess(request, response, accessToken, refreshToken);
        } else {
            handleAndroidSuccess(request, response, accessToken, refreshToken);
        }
    }

    private ClientType detectClientType(HttpServletRequest request) {
        String state = request.getParameter("state");

        if (state != null && state.contains(ANDROID)) {
            return ClientType.ANDROID;
        }
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        if (userAgent != null && userAgent.contains(ANDROID)) {
            return ClientType.ANDROID;
        }

        String clientType = request.getParameter("X-Client-Type");
        if (ANDROID.equalsIgnoreCase(clientType)) {
            return ClientType.ANDROID;
        }

        return ClientType.WEB;
    }

    private void handleAndroidSuccess(
            HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        String deepLink = String.format(
                "%s://oauth2redirect/callback?accessToken=%s&refreshToken=%s",
                androidDeepLinkScheme,
                URLEncoder.encode(accessToken, StandardCharsets.UTF_8),
                URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
        getRedirectStrategy().sendRedirect(request, response, deepLink);
    }

    private void handleWebSuccess(
            HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendPath)
                .path("/oauth-success")
                .fragment("accessToken=%s&refreshToken=%s&tokenType=Bearer"
                        .formatted(
                                URLEncoder.encode(accessToken, StandardCharsets.UTF_8),
                                URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)))
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private enum ClientType {
        ANDROID,
        WEB;
    }
}
