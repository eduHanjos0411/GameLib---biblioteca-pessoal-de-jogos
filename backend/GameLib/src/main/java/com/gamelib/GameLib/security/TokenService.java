package com.gamelib.GameLib.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gamelib.GameLib.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  @Value("${api.security.token.expiration-ms}")
  private Long expirationMs;

  private SecretKey getSigninKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String gerarToken(Usuario usuario) {
    Date hoje = new Date();
    Date dataExpiracao =  new Date(hoje.getTime() + expirationMs);

    return Jwts.builder()
      .subject(usuario.getEmail())
      .claim("id", usuario.getId())
      .claim("nome", usuario.getNome())
      .issuedAt(hoje)
      .expiration(dataExpiracao)
      .signWith(getSigninKey())
      .compact();
  }

  public String validarTokenVoltarSubject(String token) {
    try {
      Claims claims = Jwts.parser()
        .verifyWith(getSigninKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

        return claims.getSubject();
    } catch (Exception e) {
      return null;
    }
  }
}
