package com.dsw.practica02.empleados.config;

import com.dsw.practica02.empleados.repository.EmpleadoRepository;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ADMIN_ROLE = "ADMIN";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/empleados").permitAll()
                        .requestMatchers("/api/v1/empleados/**").hasRole(ADMIN_ROLE)
                        .anyRequest().hasRole(ADMIN_ROLE)
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(EmpleadoRepository empleadoRepository) {
        return username -> {
            String clave = username == null ? null : username.trim().toUpperCase(Locale.ROOT);
            return empleadoRepository.findByClaveIgnoreCase(clave)
                    .map(empleado -> User.builder()
                            .username(empleado.getClave())
                            .password(empleado.getPassword())
                            .roles(ADMIN_ROLE)
                            .build())
                    .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                            "Empleado no encontrado"));
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
