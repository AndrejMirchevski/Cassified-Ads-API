package com.example.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

        private static final String API_KEY = "demo-key-2026";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //to get the key form the header
        String providedKey = request.getHeader("X-API-Key");

        //if authorized allow the request
        if (API_KEY.equals(providedKey)) {
            return true;
        }

        //return 401 if not authorized
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing API Key");
        return false;
    }
}