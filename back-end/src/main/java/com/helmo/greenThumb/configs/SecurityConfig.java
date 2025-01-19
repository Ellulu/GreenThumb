package com.helmo.greenThumb.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS configuration
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/public/**", "/api/plants", "/api/**").permitAll()// Toutes les autres requêtes doivent être authentifiées
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Ajoute le filtre JWT

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("https://green-thumb-five.vercel.app/"); // Autoriser toutes les origines

        configuration.addAllowedMethod("*"); // Autoriser toutes les méthodes (GET, POST, etc.)
        configuration.addAllowedHeader("*"); // Autoriser tous les headers
        configuration.setAllowCredentials(true); // Désactive les cookies/credentials (indispensable avec "*")

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique à toutes les routes

        System.out.println("CORS configuration applied too");

        return source;
    }
}
