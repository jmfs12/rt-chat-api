package com.jmfs.api.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.jmfs.api.domain.User;
import com.jmfs.api.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService{
    @Value("${api.security.token.secret}")
    private String SECRET_KEY;

    public String generateToken(User user){
        try{            
            log.info("[TOKEN SERVICE] Generating token for user: {}", user.getUsername());
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    
            String token = JWT.create()
                    .withIssuer("jmfs")
                    .withClaim("id", user.getId())
                    .withSubject(user.getUsername())
                    .withExpiresAt(getExpirationTime())
                    .sign(algorithm);
            log.info("[TOKEN SERVICE] Token generated successfully for user: {}", user.getUsername());
            return token;
        } catch(JWTCreationException e){
            log.error("[TOKEN SERVICE] Error generating token for user: {}", user.getUsername(), e);
            throw new RuntimeException("Error generating token");
        }
    }

    public String validateToken(String token){
        try{
            log.info("[TOKEN SERVICE] Validating token");
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer("jmfs")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            log.error("[TOKEN SERVICE] Invalid token", e);
            return null;
        }
    }

    public Long extractUserId(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("id").asLong();
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-3"));
    }
}
