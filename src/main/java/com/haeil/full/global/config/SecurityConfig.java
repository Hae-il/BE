package com.haeil.full.global.config;

import com.haeil.full.auth.filter.JwtAuthFilter;
import com.haeil.full.auth.util.JwtTokenProvider;
import com.haeil.full.global.exception.handler.CustomAccessDeniedHandler;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS)) // JWT이므로 STATELESS 유지
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling.accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                // Static resources
                                                "/css/**",
                                                "/js/**",
                                                "/images/**",
                                                "/webjars/**",
                                                // Public MVC pages
                                                "/",
                                                "/auth/**",
                                                "/lawfirm/**",
                                                "/api/v1/chatbot/ask",
                                                "/api/v1/chatbot/reservation",
                                                "/api/v1/auth/**")
                                        .permitAll()
                                        .requestMatchers("/cases/unassigned/**")
                                        .hasAnyRole("SECRETARY", "ADMIN")
                                        .requestMatchers(
                                                "/cases/requested/**",
                                                "/cases/ongoing/**",
                                                "/cases/completed/**")
                                        .hasAnyRole("ATTORNEY", "ADMIN")
                                        .requestMatchers("/consultations/**")
                                        .hasAnyRole("SECRETARY", "ATTORNEY", "COUNSEL", "ADMIN")
                                        .requestMatchers("/settlements/**")
                                        .hasAnyRole("SECRETARY", "ACCOUNT", "ADMIN")
                                        .requestMatchers("/contracts/**")
                                        .hasAnyRole("SECRETARY", "ACCOUNT", "ADMIN")
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(
                        new JwtAuthFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 유지
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "X-Requested-With",
                        "Content-Type",
                        "Authorization",
                        "X-XSRF-token",
                        "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
