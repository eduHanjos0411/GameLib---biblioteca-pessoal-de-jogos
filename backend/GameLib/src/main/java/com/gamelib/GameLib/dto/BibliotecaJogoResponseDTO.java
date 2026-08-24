package com.gamelib.GameLib.dto;

import java.time.LocalDate;

import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.model.BibliotecaJogo;

public record BibliotecaJogoResponseDTO(
    Long id,
    Long idJogo,
    String titulo,
    String urlCapa,
    String categoria,
    String plataforma,
    StatusJogo statusAndamento,
    Integer nota,
    String opiniao,
    LocalDate dataAdicao) {
  public static BibliotecaJogoResponseDTO fromEntity(BibliotecaJogo entity) {
    return new BibliotecaJogoResponseDTO(
      entity.getId(),
      entity.getJogo().getId(),
      entity.getJogo().getTitulo(),
      entity.getJogo().getUrlCapa(),
      entity.getJogo().getCategoria(),
      entity.getPlataforma(),
      entity.getStatusAndamento(),
      entity.getNota(),
      entity.getOpiniao(),
      entity.getDataAdicao());
  }
}
