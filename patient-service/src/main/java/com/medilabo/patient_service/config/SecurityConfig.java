package com.medilabo.patient_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration de la sécurité pour le microservice Patient.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Définit la chaîne de filtres de sécurité.
     *
     * @param http L'objet de configuration HTTP.
     * @return La chaîne de filtres configurée.
     * @throws Exception En cas d'erreur de configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults());

        return http.build();
    }
}