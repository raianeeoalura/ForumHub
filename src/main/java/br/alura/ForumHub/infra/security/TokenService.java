package br.alura.ForumHub.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.alura.ForumHub.infra.persistence.entity.UserDB;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(UserDB user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      String token = JWT.create()
          .withIssuer("ForumHub")
          .withSubject(user.getUsername())
          .withClaim("id", user.getId().toString())
          .withExpiresAt(genExpirationDate())
          .sign(algorithm);

      return token;
    } catch (JWTCreationException e) {
      throw new RuntimeException("Error generating JWT token", e);
    }
  }

  private Instant genExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // Brasilia timezone
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .withIssuer("ForumHub")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException e) {
      return "";
    }
  }
}
