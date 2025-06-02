package com.jwt.config;

import com.jwt.service.CustomEmployeeDetailsService;
import com.jwt.utility.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private JwtUtil jwtUtil;
    private CustomEmployeeDetailsService customEmployeeDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token=((JwtAuthenticationToken) authentication).getToken();
        String userName=jwtUtil.validateAndExtractUserName(token);
        UserDetails userDetails=customEmployeeDetailsService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
