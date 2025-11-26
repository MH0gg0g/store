package com.example.store.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                        AccessDeniedException accessDeniedException) throws IOException, ServletException {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = (authentication != null)
                                ? authentication.getName()
                                : "Anonymos";

                log.warn("Access denied user: {}. attempted to acces resource: {} - {}", email,
                                request.getRequestURI(),
                                accessDeniedException.getMessage());

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getOutputStream().println("{ \"error\": \"" + accessDeniedException.getMessage() + "\" }");
        }
}
