package com.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.PrintWriter;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
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
            System.out.println(jwtToken);
            response.setHeader("Authorization","Bearer "+jwtToken);
            // >>>>> ADD THIS TO SEND A RESPONSE FROM THE FILTER <<<<<
            response.setContentType("application/json"); // Or "text/plain" if you prefer
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            PrintWriter out = response.getWriter();
            // You can send a JSON success message, or even just the token itself in the body
            out.print("{\"status\":\"success\", \"message\":\"Token generated\", \"token\":\"Bearer " + jwtToken + "\"}");
            // Or just: out.print("Token generated.");
            out.flush();
            // IMPORTANT: Do NOT call filterChain.doFilter() after this if the filter handles the response
            return; // Explicitly return to prevent further processing if this filter handles the response.
        }
    }
}
