package com.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.service.CustomEmployeeDetailsService;
import com.jwt.utility.JwtUtil;
import com.jwt.utility.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.runtime.ObjectMethods;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public JwtUtil jwtUtil;
    public AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getServletPath().equals("/employee/registerToken")){
            filterChain.doFilter(request,response);
            return;
        }
        ObjectMapper objectMapper=new ObjectMapper();
        LoginRequest loginRequest=objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(loginRequest.getName(),loginRequest.getPassword());
        Authentication authentication=authenticationManager.authenticate(token);
        if(authentication.isAuthenticated()){
            String jwtToken= jwtUtil.generateToken(authentication.getName(), 15);
            response.setHeader("Authorization","Bearer "+jwtToken);
        }
    }
}
