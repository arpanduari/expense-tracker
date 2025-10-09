package dev.arpan.expensetracker.config.security;

import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * @author arpan
 * @since 10/9/25
 */
@Service
@RequiredArgsConstructor
public class GoogleOauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        return processOAuth2User(registrationId, oAuth2User);
    }

    private OAuth2User processOAuth2User(String provider, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");

        User user = userRepository.findByEmail(email).orElseGet(() ->
                User.builder()
                        .email(email)
                        .username(name != null ? name : email.split("@")[0])
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .secureUrl(picture)
                        .isVerified(true)
                        .oauthProviderId(providerId)
                        .oauthProvider(provider)
                        .build()
        );
        if (name != null && !name.equals(user.getUsername())) {
            user.setUsername(name);
        }
        if (picture != null && !picture.equals(user.getSecureUrl())) {
            user.setSecureUrl(picture);
        }
        if (!provider.equals(user.getOauthProvider())) {
            user.setOauthProvider(provider);
        }
        if (!providerId.equals(user.getOauthProviderId())) {
            user.setOauthProviderId(providerId);
        }
        userRepository.save(user);

        return new CustomOAuth2User(
                user,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
