package com.gamelib.GameLib.dto;

import com.gamelib.GameLib.enums.StatusJogo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AdicionarJogoBibliotecaDTO(
  @NotBlank(message = "O título do jogo é obrigatório.")
    String titulo,

    @NotBlank(message = "A plataforma é obrigatória.")
    String plataforma,

    StatusJogo statusAndamento,

    @Min(value = 0, message = "A nota mínima é 0.")
    @Max(value = 10, message = "A nota máxima é 10.")
    Integer nota,

    String opiniao,
    String categoria,
    String urlCapa,
    String apiExternalId
) {

}
