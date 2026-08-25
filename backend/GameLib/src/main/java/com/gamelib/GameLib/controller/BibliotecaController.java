package com.gamelib.GameLib.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamelib.GameLib.dto.AdicionarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.AtualizarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.BibliotecaJogoResponseDTO;
import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.service.BibliotecaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/biblioteca")
public class BibliotecaController {

  private final BibliotecaService bibliotecaService;
  
  public BibliotecaController(BibliotecaService bibliotecaService) {
    this.bibliotecaService = bibliotecaService;
  }

  @PostMapping
  public ResponseEntity<BibliotecaJogoResponseDTO> adicionarJogo(
    @RequestHeader("X-Usuario-Id") Long usuarioId, @Valid @RequestBody AdicionarJogoBibliotecaDTO dto) {

    BibliotecaJogoResponseDTO response = bibliotecaService.adicionarJogo(usuarioId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<BibliotecaJogoResponseDTO>> listarBiblioteca(
    @RequestHeader("X-Usuario-Id") Long usuarioId, @RequestParam(required = false) StatusJogo status) {
    
    if (status != null) {
      return ResponseEntity.ok(bibliotecaService.listarPorStatus(usuarioId, status));
    }
    return ResponseEntity.ok(bibliotecaService.listarBibliotecaPorUsuario(usuarioId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BibliotecaJogoResponseDTO> atualizarItem(
    @RequestHeader("X-Usuario-Id") Long usuarioId,
    @PathVariable Long id,
    @Valid @RequestBody AtualizarJogoBibliotecaDTO dto) {

    BibliotecaJogoResponseDTO response = bibliotecaService.atualizarItem(usuarioId, id, dto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removerItem(
    @RequestHeader("X-Usuario-Id") Long usuarioId,
    @PathVariable Long id) {

    bibliotecaService.removerItem(usuarioId, id);
    return ResponseEntity.noContent().build();
  }


  
}
