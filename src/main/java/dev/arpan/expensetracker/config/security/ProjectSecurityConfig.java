package dev.arpan.expensetracker.config.security;

import dev.arpan.expensetracker.auth.AuthService;
import dev.arpan.expensetracker.common.filter.JWTTokenValidatorFilter;
import dev.arpan.expensetracker.common.filter.RateLimiterFilter;
import dev.arpan.expensetracker.config.properties.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author arpan
 * @since 8/3/25
 */
@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {
    private final ApiProperties apiProperties;
    private final JWTTokenValidatorFilter jwtTokenValidatorFilter;
    private final RateLimiterFilter rateLimiterFilter;
    private final GoogleOauth2Service googleOAuth2Service;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;
    /**
     * Defines the application's security configuration.
     * Separates concerns: authorization rules, filters, OAuth, stateless auth.
     * Uses JWT + stateless requests instead of session-based authentication.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, AuthService authService)
            throws Exception {

        // Apply all authorization/permit rules in a separate method
        // keeps this bean readable and avoids long method chains inline.
        httpSecurity.authorizeHttpRequests(authorizeRequests());

        // Force stateless authentication since JWT is used.
        // Server does NOT store session, client must send token every request.
        httpSecurity.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Disable CSRF protection because stateless REST APIs
        // don't rely on cookies, so CSRF isn't required.
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Disable default login page since we authenticate using tokens.
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);

        // Disable basic auth header-based authentication.
        // Prevents accidental exposure of credentials.
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        configureFilters(httpSecurity);
        configureOAuth(httpSecurity);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        CustomUsernamePasswordAuthenticationProvider authenticationProvider =
                new CustomUsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(List.of(authenticationProvider));
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
            authorizeRequests() {
        return request -> request.requestMatchers(apiProperties.getFullPath() + "/auth/**")
                .permitAll()
                .requestMatchers(apiProperties.getFullPath() + "/auth/change-password")
                .authenticated()
                .requestMatchers(apiProperties.getFullPath() + "/ledger/shared-entries")
                .permitAll()
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    private void configureFilters(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class);
        httpSecurity.addFilterBefore(rateLimiterFilter, BasicAuthenticationFilter.class);
    }

    private void configureOAuth(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2Service))
                .successHandler(oauth2LoginSuccessHandler));
    }
}
