package dev.expensewise.backend.config.security;

import dev.expensewise.backend.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * @author arpan
 * @since 10/9/25
 */
public class CustomOAuth2User extends DefaultOAuth2User {
    private final User user;

    public CustomOAuth2User(User user, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
