package com.bfs.hibernateprojectdemo.security;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.bfs.hibernateprojectdemo.dto.UserDTO;
import com.bfs.hibernateprojectdemo.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.key}")
    private String secretKey;
    
    @Value("${security.jwt.token.expiration}")
    private long validityInMilliseconds;


    public String generateToken(UserDTO userDTO) {
        // Normalize permissions to lowercase
        List<String> permissions = userDTO.getPermissions()
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());
        
        // Normalize the role to uppercase (assuming role is a single value)
        String role = userDTO.getRole().name().toUpperCase();

        long validityInMilliseconds = 7200000; // Example: 2 hours

        return Jwts.builder()
                .setSubject(userDTO.getUsername())
                .claim("permissions", permissions)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }



    public Optional<AuthUserDetail> resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authorization header missing or does not start with Bearer");
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            List<String> permissions = claims.get("permissions", List.class);
            String role = claims.get("role", String.class); 

            List<String> roles = List.of(role);

            List<GrantedAuthority> authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            authorities.addAll(roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList()));

            return Optional.of(AuthUserDetail.builder()
                    .username(username)
                    .authorities(authorities)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        } catch (Exception e) {
            throw new InvalidTokenException("Error while resolving token: " + e.getMessage(), e);
        }
    }








}
