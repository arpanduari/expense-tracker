package dev.expensewise.backend.config.security;

import dev.expensewise.backend.messaging.account.AccountCreatedMessageProducer;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
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
    private final AccountCreatedMessageProducer accountCreatedMessageProducer;

    @Value("${app.frontend.path}")
    private String frontendPath;

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
        String name = email.split("@")[0];
        String picture = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");
        boolean isNewUser = false;

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            isNewUser = true;
            user = User.builder()
                    .email(email)
                    .username(name)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .secureUrl(picture)
                    .isVerified(true)
                    .oauthProviderId(providerId)
                    .oauthProvider(provider)
                    .build();
        }

        if (name != null && !name.equals(user.getUsername())) {
            user.setUsername(name);
        }
        if (picture != null && !picture.equals(user.getSecureUrl())) {
            user.setSecureUrl(picture);
        }
        if (!provider.equals(user.getOauthProvider())) {
            user.setOauthProvider(provider);
        }
        if (providerId != null && !providerId.equals(user.getOauthProviderId())) {
            user.setOauthProviderId(providerId);
        }

        userRepository.save(user);

        if (isNewUser) {
            accountCreatedMessageProducer.sendAccountCreatedMessage(email, name, frontendPath + "/login");
        }

        return new CustomOAuth2User(
                user,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email");
    }
}
