package br.edu.ifpb.dac.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final String SECRET = "secret-password";

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String extractUsername(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
    }

    public List<String> extractRoles(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getClaim("roles")
                .asList(String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Date expiration = JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getExpiresAt();
        return (username.equals(userDetails.getUsername()) && expiration.after(new Date()));
    }
}