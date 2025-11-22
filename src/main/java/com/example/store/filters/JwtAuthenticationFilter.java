package com.example.store.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.store.dtos.ErrorDto;
import com.example.store.exceptions.InvalidTokenException;
import com.example.store.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var jwt = jwtService.ExtractJwtFromRequest(request);
            if (jwt != null && !jwt.isExpired() && !jwtService.isTokenBlacklisted(jwt.getTokenID())) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        jwt.getUserID(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getRole())));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            var error = new ErrorDto(HttpStatus.UNAUTHORIZED, ex.getMessage());
            var mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(error));
        }
    }
}
