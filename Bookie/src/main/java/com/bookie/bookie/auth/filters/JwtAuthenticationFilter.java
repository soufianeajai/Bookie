package com.bookie.bookie.auth.filters;

import com.bookie.bookie.services.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        try {
            String authHeader = request.getHeader("Authorization");
            String prefix = "Bearer ";
            if (authHeader == null || !authHeader.startsWith(prefix) || request.getRequestURI().endsWith("/login")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(prefix.length());
            if (jwtService.isTokenValid(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String userEmail = jwtService.extractUsername(token);
                    List<String> roles = jwtService.extractRoles(token);
                    var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}