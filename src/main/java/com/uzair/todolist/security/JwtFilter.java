package com.uzair.todolist.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger ( JwtFilter.class );
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest ( request );

            if ( StringUtils.hasText ( jwt ) ) {
                String username = jwtUtil.extractUsername ( jwt );

                if ( StringUtils.hasText ( username ) && SecurityContextHolder.getContext ( ).getAuthentication ( ) == null ) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername ( username );

                    if ( jwtUtil.validateToken ( jwt, userDetails ) ) {
                        authenticateUser ( userDetails, request );
                        logger.debug ( "User authenticated successfully: {}", username );
                    }
                }
            }

            chain.doFilter ( request, response );
        }
        catch ( Exception e ) {
            logger.error ( "Cannot authenticate user: {}", e.getMessage ( ) );
            chain.doFilter ( request, response );
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader ( AUTHORIZATION_HEADER );
        log.info("Processing request with Authorization: {}", bearerToken != null);
        if ( StringUtils.hasText ( bearerToken ) && bearerToken.startsWith ( BEARER_PREFIX ) ) {
            return bearerToken.substring ( BEARER_PREFIX.length ( ) );
        }
        return null;
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken (
                userDetails,
                null,
                userDetails.getAuthorities ( )
        );
        authentication.setDetails ( new WebAuthenticationDetailsSource ( ).buildDetails ( request ) );
        SecurityContextHolder.getContext ( ).setAuthentication ( authentication );
    }
}