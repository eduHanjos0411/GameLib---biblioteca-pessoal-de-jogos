package com.gamelib.GameLib.dto;

public record TokenResponseDTO(
    String token,
    String type,
    Long usuarioId,
    String nome) {
  public TokenResponseDTO(String token, Long usuarioId, String nome) {
    this(token, "Bearer", usuarioId, nome);
  }
}
