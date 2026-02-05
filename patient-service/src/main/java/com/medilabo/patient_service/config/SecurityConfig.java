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
                // Désactivation de la protection CSRF pour permettre les requêtes POST/PUT/DELETE sur l'API
                .csrf(csrf -> csrf.disable())

                // Autorisation de l'affichage dans des frames (nécessaire pour la console H2)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // Configuration des règles d'accès aux endpoints
                .authorizeHttpRequests(auth -> auth
                        // Accès libre à la console H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Authentification requise pour tous les autres appels API
                        .anyRequest().authenticated()
                )

                // Activation de l'authentification Basic (pour les tests API et l'inter-service)
                .httpBasic(withDefaults())
                // Activation du formulaire de login par défaut
                .formLogin(withDefaults());

        return http.build();
    }
}