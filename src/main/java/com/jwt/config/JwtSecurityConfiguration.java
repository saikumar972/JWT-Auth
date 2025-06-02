package com.jwt.config;

import com.jwt.service.CustomEmployeeDetailsService;
import com.jwt.utility.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class JwtSecurityConfiguration {
    public CustomEmployeeDetailsService customEmployeeDetailsService;
    public JwtUtil jwtUtil;
    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        JwtAuthenticationFilter jwtAuthenticationFilter =new JwtAuthenticationFilter(jwtUtil,authenticationManager);
        JwtTokenValidationFilter jwtTokenValidationFilter= new JwtTokenValidationFilter(authenticationManager);
        http.authorizeHttpRequests(
                auth->auth.requestMatchers("/employee/add","employee/registerToken").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtTokenValidationFilter, JwtAuthenticationFilter.class)
                .csrf(csrf->csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080")); // Or your Postman/client origin
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*")); // Allows all headers
                    configuration.setExposedHeaders(List.of("Authorization", "Content-Type")); // << EXPOSE Authorization
                    configuration.setAllowCredentials(true);
                    return configuration;
                }));
                return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customEmployeeDetailsService);
        return authenticationProvider;
    }
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider(jwtUtil,customEmployeeDetailsService);
    }
    @Bean
    public AuthenticationManager providerManager(){
        return new ProviderManager(Arrays.asList(daoAuthProvider(),
                jwtAuthenticationProvider()
        ));
    }
}
