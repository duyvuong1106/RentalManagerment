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

                .cors(cors -> {})
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
                        "/api/amenities/**",

                        "/api/services",
                        "/api/services/**",

                        "/api/rooms",
                        "/api/rooms/**"
                ).permitAll()

                
                .requestMatchers(
                        HttpMethod.POST,

                        "/api/areas",
                        "/api/room-types",
                        "/api/amenities",
                        "/api/services"
                ).hasRole("ADMINISTRATOR")

                .requestMatchers(
                        HttpMethod.PUT,

                        "/api/areas/**",
                        "/api/room-types/**",
                        "/api/amenities/**",
                        "/api/services/**"
                ).hasRole("ADMINISTRATOR")

                .requestMatchers(
                        HttpMethod.DELETE,

                        "/api/areas/**",
                        "/api/room-types/**",
                        "/api/amenities/**",
                        "/api/services/**"
                ).hasRole("ADMINISTRATOR")

                
                .requestMatchers(
                        "/api/landlord/**"
                ).hasRole("LANDLORD")

                
                .requestMatchers(
                        "/api/customer/**"
                ).hasRole("CUSTOMER")

                .requestMatchers(
                        "/api/profile",
                        "/api/profile/**"
                ).authenticated()

                
                .requestMatchers(
                        "/api/admin/reviews",
                        "/api/admin/reviews/**"
                ).hasRole("ADMINISTRATOR")

                .requestMatchers(
                        "/api/admin/rooms",
                        "/api/admin/rooms/**"
                ).hasRole("ADMINISTRATOR")

                .requestMatchers(
                        "/api/admin/statistics",
                        "/api/admin/statistics/**"
                ).hasRole("ADMINISTRATOR")

                
                .requestMatchers(
                        "/api/users/**"
                ).hasRole("ADMINISTRATOR")

                
                .requestMatchers(
                        "/admin/**"
                ).hasRole("ADMINISTRATOR")

                
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()

                
                .anyRequest().authenticated()
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