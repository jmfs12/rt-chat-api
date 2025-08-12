package com.jmfs.api.config;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.jmfs.api.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor{
    private final TokenService tokenService;

    public AuthHandshakeInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String token = null;

            // Try header first
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                // Fallback to query param parsing using URL utilities
                String query = httpRequest.getQueryString();
                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length == 2 && keyValue[0].equals("token")) {
                            token = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                            break;
                        }
                    }
                }
            }

            if (token == null) {
                // No token in header or query, allow handshake (for SockJS fallback)
                return true;
            }

            var userDetails = tokenService.validateToken(token);
            if (userDetails != null) {
                attributes.put("user", new StompPrincipal(userDetails));
                return true;
            }
        }

        // Deny if token invalid or request is not an instance of ServletServerHttpRequest
        return false;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {}
}
