package com.anitalk.app.config;

import com.anitalk.app.security.JwtAuthenticateFilter;
import com.anitalk.app.security.JwtGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/**").permitAll()
//                                .requestMatchers(HttpMethod.GET).permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/animations/*/reviews").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
//                                .requestMatchers(HttpMethod.PUT).authenticated()
//                                .requestMatchers(HttpMethod.DELETE).authenticated()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticateFilter jwtAuthenticateFilter(JwtGenerator generator){
        return new JwtAuthenticateFilter(generator);
    }
}
