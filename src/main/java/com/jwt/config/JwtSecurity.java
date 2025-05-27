package com.jwt.config;

import com.jwt.service.CustomEmployeeDetailsService;
import com.jwt.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class JwtSecurity {
    public CustomEmployeeDetailsService customEmployeeDetailsService;
    public JwtUtil jwtUtil;
    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        JwtFilter jwtFilter=new JwtFilter(jwtUtil,authenticationManager);
        http.authorizeHttpRequests(
                auth->auth.requestMatchers("/employee/add").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf->csrf.disable());
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
    public AuthenticationManager providerManager(){
        return new ProviderManager(Arrays.asList(daoAuthProvider()));
    }
}
