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
                // 1. Désactiver la protection CSRF uniquement pour la console H2
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))

                // 2. Autoriser l'affichage dans des frames pour H2
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // 3. Définir les règles d'accès
                .authorizeHttpRequests(auth -> auth
                        // On laisse passer tout le monde sur la console H2 (elle a son propre login)
                        .requestMatchers("/h2-console/**").permitAll()
                        // Tout le reste de l'API nécessite d'être connecté (user/password)
                        .anyRequest().authenticated()
                )

                // 4. Activer l'authentification Basic (pour tes tests et Postman)
                .httpBasic(withDefaults())
                // 5. Activer le formulaire de login classique (optionnel mais sympa)
                .formLogin(withDefaults());

        return http.build();
    }
}