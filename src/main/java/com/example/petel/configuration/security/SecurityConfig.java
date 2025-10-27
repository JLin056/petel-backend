package com.example.petel.configuration.security;

import com.example.petel.model.ReturnCodeAndDescEnum;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /** JwtAuthFilter */
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                // 做 JWT，關閉 CSRF
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(sm -> sm.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                // cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 例外處理
                .exceptionHandling(ex -> ex
                        // 未認證
                        .authenticationEntryPoint((req, res, authEx) -> {
                            SecurityErrorResponseWriter.writeError(
                                    res,
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ReturnCodeAndDescEnum.UNAUTHORIZED,
                                    "JWT 無效 或 未登入"
                            );
                        })
                        // 已認證但沒權限
                        .accessDeniedHandler((req, res, ex2) -> {
                            SecurityErrorResponseWriter.writeError(
                                    res,
                                    HttpServletResponse.SC_FORBIDDEN,
                                    ReturnCodeAndDescEnum.FORBIDDEN,
                                    "權限不足"
                            );
                        })
                )

                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll() // 開發階段先把所有都打開
//                        .requestMatchers(HttpMethod.POST,
//                            "/auth/register", "/auth/login",
//                            "/auth/forgot-password", "/auth/reset-password",
//                            "/auth/verify", "/auth/check", "/auth/profile/check",
//                            "/auth/refresh").permitAll()
//                        .requestMatchers("/auth/me").authenticated()
//                        .requestMatchers("/hotels/**").permitAll()
//                        .anyRequest()
//                        .authenticated()
                )

                .formLogin(login -> login.disable())
                .httpBasic(basic -> basic.disable());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 設定 CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:4200")); // 前端 Origin
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        cfg.setAllowCredentials(true); // Cookie 帶 JWT
        cfg.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

}
