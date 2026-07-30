package com.nixon.challengelab.config;

import com.nixon.challengelab.exceptions.handler.CustomAccessDeniedHandler;
import com.nixon.challengelab.exceptions.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfiguration {

    private final JwtAuthenticationFilter authenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(accessDeniedHandler);
                    handler.authenticationEntryPoint(authenticationEntryPoint);
                })
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/challenges",
                                "/api/v1/challenges/*/submissions",
                                "/api/v1/challenges/*/teams",
                                "/api/v1/teams/*/join",
                                "/api/v1/ratings/submissions/*",
                                "/api/v1/submissions/*/winner"
                        ).hasAnyRole("ADMIN", "INDIVIDUAL", "PROFESSIONAL")

                        .requestMatchers(
                                GET,
                                "/api/v1/challenges",
                                "/api/v1/challenges/*",
                                "/api/v1/challenges/*/teams",
                                "/api/v1/challenges/*/submissions",
                                "/api/v1/users/*/challenges",
                                "/api/v1/users/by-identifier/*/challenges",
                                "/api/v1/challenges/me",
                                "/api/v1/teams/*",
                                "/api/v1/teams/*/submissions",
                                "/api/v1/team-members/**",
                                "/api/v1/submissions/me",
                                "/api/v1/submissions/*",
                                "/api/v1/ratings/submissions/*",
                                "/api/v1/ratings/submissions/*/average"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/challenges/*",
                                "/api/v1/users/me"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/challenges/*/status"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/challenges/*",
                                "/api/v1/teams/*/leave",
                                "/api/v1/submissions/*"
                        ).authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setMaxAge(3200L);
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));
        configuration.setAllowedMethods(List.of(
                GET.name(),
                POST.name(),
                PUT.name(),
                DELETE.name(),
                PATCH.name()
        ));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of(
                "Authorization",
                "X-Get-Header",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
