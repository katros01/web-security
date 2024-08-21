package com.gtp2.web.security.Interceptors;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtp2.web.security.Enum.Role;
import com.gtp2.web.security.utils.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getRequestURI().startsWith("/api/v1/users") && request.getMethod().equalsIgnoreCase("POST")) {
            Role role =(Role) request.getSession().getAttribute("role");
            if (role == null) {
                CustomResponse<String> customResponse = new CustomResponse<>(
                        "Unauthorized access",
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "UnAuthorized"
                );
                writeResponse(response, customResponse, HttpServletResponse.SC_UNAUTHORIZED);

                return false;
            } else if (!role.equals(Role.ADMIN)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access Denied: You do not have permission to perform this action.");
                return false;
            }
        }

        return true;
    }

    private void writeResponse(HttpServletResponse response, CustomResponse<?> customResponse, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(customResponse);
        response.getWriter().write(jsonResponse);
    }

}
