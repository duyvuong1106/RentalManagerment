package com.nldv.rentalroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/auth/register",
                        "/api/auth/login"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/areas",
                        "/api/areas/**",
                        "/api/room-types",
                        "/api/room-types/**",
                        "/api/amenities",
                        "/api/amenities/**"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/areas"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/room-types"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/amenities"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/areas/**"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/room-types/**"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/amenities/**"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/areas/**"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/room-types/**"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/amenities/**"
                ).hasRole("ADMINISTRATOR")
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()
                .requestMatchers("/admin/**")
                .hasRole("ADMINISTRATOR")
                .requestMatchers("/api/customer/**")
                .hasRole("CUSTOMER")
                .requestMatchers("/api/landlord/**")
                .hasRole("LANDLORD")
                .requestMatchers("/api/users/**")
                .hasRole("ADMINISTRATOR")
                .anyRequest()
                .authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin/rooms", true)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                );

        return http.build();
    }
}
