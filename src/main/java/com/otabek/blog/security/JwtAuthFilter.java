package com.otabek.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component  
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(
        JwtAuthFilter.class
    );

    private final JwtUtils jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
    
        final String authHeader = request.getHeader("Authorization");
    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        final String jwt = authHeader.substring(7);
    
        try {
            if (!jwtUtil.validateJwtToken(jwt)) {
                logger.warn("Invalid JWT token");
                filterChain.doFilter(request, response);
                return;
            }
    
            String username = jwtUtil.getUserNameFromJwtToken(jwt);
    
            if (
                username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null
            ) {
                UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);
    
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
    
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
    
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
    
        } catch (Exception e) {
            logger.error("JWT authentication failed", e);
        }
    
        filterChain.doFilter(request, response);
    }
}
