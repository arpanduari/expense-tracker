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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, AuthService authService) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(apiProperties.getFullPath() + "/auth/**")
                .permitAll()
                .requestMatchers(apiProperties.getFullPath() + "/auth/change-password")
                .authenticated()
                .requestMatchers(apiProperties.getFullPath() + "/ledger/shared-entries")
                .permitAll()
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        );
        httpSecurity.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class);
        httpSecurity.addFilterBefore(rateLimiterFilter, BasicAuthenticationFilter.class);
        httpSecurity.oauth2Login(
                oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2Service))
                        .successHandler(oauth2LoginSuccessHandler)
        );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        CustomUsernamePasswordAuthenticationProvider authenticationProvider = new CustomUsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
