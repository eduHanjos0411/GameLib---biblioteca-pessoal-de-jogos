package com.gamelib.GameLib.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamelib.GameLib.dto.external.RawgGameResponseDTO;
import com.gamelib.GameLib.service.RawgApiService;

@RestController
@RequestMapping("/api/v1/jogos-externos")
public class JogoExternoController {

  private final RawgApiService rawgApiService;

  public JogoExternoController(RawgApiService rawgApiService) {
    this.rawgApiService = rawgApiService;
  }

  @GetMapping("/buscar")
    public ResponseEntity<List<RawgGameResponseDTO>> buscar(@RequestParam String nome) {
        List<RawgGameResponseDTO> resultados = rawgApiService.buscarJogosPorNome(nome);
        return ResponseEntity.ok(resultados);
    }
}
