package com.gamelib.GameLib.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamelib.GameLib.dto.LoginDTO;
import com.gamelib.GameLib.dto.TokenResponseDTO;
import com.gamelib.GameLib.dto.UsuarioCadastroDTO;
import com.gamelib.GameLib.dto.UsuarioResponseDTO;
import com.gamelib.GameLib.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
  
  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping("/cadastrar")
  public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO dto) {
    UsuarioResponseDTO response = usuarioService.cadastrarUsuario(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
    TokenResponseDTO response = usuarioService.autenticar(dto);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
    UsuarioResponseDTO response = usuarioService.buscarPorId(id);
    return ResponseEntity.ok(response);
  }
}
