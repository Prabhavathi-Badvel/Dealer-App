package com.dlerin.application.securities;


import com.dlerin.application.exception.DlerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        logger.error("Unauthorized error. Message - {}", e.getMessage());
        // Determine the actual exception and customize the response based on it
        if ( e.getCause() instanceof RuntimeException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error -> Bad Request");
        } else if (e.getCause() instanceof DlerNotFoundException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User Not Found");
        } else {
            logger.error("Unauthorized error. Message - {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Unauthorized");
        }
    }
}

