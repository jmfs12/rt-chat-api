package com.jmfs.api.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jmfs.api.domain.User;
import com.jmfs.api.repositories.UserRepository;
import com.jmfs.api.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String path = request.getRequestURI();
        log.info("[SECURITY FILTER] Processing request for path: {}", path);

        if(path.equals("/auth/login") || path.equals("/auth/register")){
            log.info("[SECURITY FILTER] Public endpoint accessed: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("[SECURITY FILTER] Secured endpoint accessed: {}", path);
        var token = recoverToken(request);
        var login = tokenService.validateToken(token);

        if(login != null){
            log.info("[SECURITY FILTER] Valid token for user: {}", login);
            User user = userRepository.findByUsername(login).orElseThrow(() -> new RuntimeException("User not found"));
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{
            log.warn("[SECURITY FILTER] Invalid or missing token");
        }
        filterChain.doFilter(request, response);

    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
        
}
