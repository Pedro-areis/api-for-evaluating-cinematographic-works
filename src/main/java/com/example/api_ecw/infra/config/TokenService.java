package com.example.api_ecw.infra.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.api_ecw.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api_ecw.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Api ECW")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

            return  token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Api ECW")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}
