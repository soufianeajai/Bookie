package com.bookie.bookie.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    
    @Value("${application.security.access-token.expiration}")
    private long jwtAccessTokenExpiration;

    @Value("${application.security.refresh-token.expiration}")
    private long jwtRefreshTokenExpiration;

    public String generateAccessToken(UserDetails user) {
        Map<String, List<String>> claims = new HashMap<>();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessTokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshTokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        parser.parseClaimsJws(token);
        return true;
    }

    public String extractUsername(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        Claims claims =  parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<String> extractRoles(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        Claims claims =  parser.parseClaimsJws(token).getBody();
        List<?> list = claims.get("roles", List.class);
        return list.stream().map(Object::toString).toList();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}