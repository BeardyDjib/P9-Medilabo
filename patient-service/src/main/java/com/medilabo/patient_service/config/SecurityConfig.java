package com.medilabo.patient_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation CSRF (nécessaire pour les POST/PUT via Postman ou Feign)
                .csrf(csrf -> csrf.disable())

                // Autorisation H2 Console
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // RÈGLES D'ACCÈS
                .authorizeHttpRequests(auth -> auth
                        // Autoriser l'accès public à TOUTES les URL (pour le dev)
                        .anyRequest().permitAll()
                )

                // On garde la config standard au cas où, mais permitAll() est prioritaire
                .httpBasic(withDefaults())
                .formLogin(withDefaults());

        return http.build();
    }
}