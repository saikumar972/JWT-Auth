package com.jwt.config;

import com.jwt.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@AllArgsConstructor
public class JwtTokenValidationFilter extends OncePerRequestFilter {
    AuthenticationManager authenticationManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String userName=null;
        if(!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        token=authHeader.substring(7);
        JwtAuthenticationToken jwtAuthenticationToken= new JwtAuthenticationToken(token);
        Authentication authenticationResult=authenticationManager.authenticate(jwtAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        filterChain.doFilter(request,response);
    }
}
