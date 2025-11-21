package com.haeil.full.global.config;

import com.haeil.full.auth.filter.JwtAuthFilter;
import com.haeil.full.auth.util.JwtTokenProvider;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS)) // JWT이므로 STATELESS 유지
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                // Static resources
                                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                                // Public MVC pages
                                                "/", "/auth/**")
                                        .permitAll()
                                        .requestMatchers("/cases/unassigned/**")
                                        .hasRole("SECRETARY")
                                        .requestMatchers(
                                                "/cases/requested/**",
                                                "/cases/ongoing/**", 
                                                "/cases/completed/**")
                                        .hasRole("ATTORNEY")
                                        .requestMatchers("/consultations/**")
                                        .hasAnyRole("SECRETARY", "ATTORNEY")
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
