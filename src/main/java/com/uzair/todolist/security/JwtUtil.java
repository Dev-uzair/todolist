package com.uzair.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
    private static final Duration TOKEN_VALIDITY = Duration.ofDays ( 2 );
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] decode = Decoders.BASE64.decode ( secret );
        this.key = Keys.hmacShaKeyFor ( decode );
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken ( new HashMap<> ( ), userDetails );
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder ( )
                .claims ( extraClaims )
                .subject ( userDetails.getUsername ( ) )
                .issuedAt ( new Date ( ) )
                .expiration ( new Date ( System.currentTimeMillis ( ) + TOKEN_VALIDITY.toMillis ( ) ) )
                .signWith ( key )
                .compact ( );
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername ( token );
        log.info ( username );
        return (username.equals ( userDetails.getUsername ( ) ) && ! isTokenExpired ( token ));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration ( token ).before ( new Date ( System.currentTimeMillis ( ) ) );
    }

    private Date extractExpiration(String token) {
        return extractClaims ( token ).getExpiration ( );
    }

    public String extractUsername(String token) {
        return extractClaims ( token ).getSubject ( );
    }

    private Claims extractClaims(String token) {
        return Jwts.parser ( )
                .verifyWith ( key )
                .build ( )
                .parseSignedClaims ( token )
                .getPayload ( );
    }
}
