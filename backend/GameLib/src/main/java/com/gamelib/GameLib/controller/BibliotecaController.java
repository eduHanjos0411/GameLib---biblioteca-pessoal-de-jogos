package com.gamelib.GameLib.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamelib.GameLib.dto.AdicionarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.AtualizarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.BibliotecaJogoResponseDTO;
import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.model.Usuario;
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
    @AuthenticationPrincipal Usuario usuarioLogado, @Valid @RequestBody AdicionarJogoBibliotecaDTO dto) {

    BibliotecaJogoResponseDTO response = bibliotecaService.adicionarJogo(usuarioLogado.getId(), dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<BibliotecaJogoResponseDTO>> listarBiblioteca(
    @AuthenticationPrincipal Usuario usuarioLogado, @RequestParam(required = false) StatusJogo status) {
    
    if (status != null) {
      return ResponseEntity.ok(bibliotecaService.listarPorStatus(usuarioLogado.getId(), status));
    }
    return ResponseEntity.ok(bibliotecaService.listarBibliotecaPorUsuario(usuarioLogado.getId()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BibliotecaJogoResponseDTO> atualizarItem(
    @AuthenticationPrincipal Usuario usuarioLogado,
    @PathVariable Long id,
    @Valid @RequestBody AtualizarJogoBibliotecaDTO dto) {

    BibliotecaJogoResponseDTO response = bibliotecaService.atualizarItem(usuarioLogado.getId(), id, dto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removerItem(
    @AuthenticationPrincipal Usuario usuarioLogado,
    @PathVariable Long id) {

    bibliotecaService.removerItem(usuarioLogado.getId(), id);
    return ResponseEntity.noContent().build();
  }


  
}
