package it.epicode.capstone.cors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CorsFilterConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public OncePerRequestFilter corsFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "https://patriciascapstone.netlify.app");
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
                response.setHeader("Access-Control-Allow-Credentials", "true");

                response.setHeader("Referrer-Policy", "no-referrer-when-downgrade");

                if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    filterChain.doFilter(request, response);
                }
            }
        };
    }
}

