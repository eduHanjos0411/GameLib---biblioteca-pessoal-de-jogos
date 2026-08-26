package com.gamelib.GameLib.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamelib.GameLib.dto.LoginDTO;
import com.gamelib.GameLib.dto.TokenResponseDTO;
import com.gamelib.GameLib.dto.UsuarioCadastroDTO;
import com.gamelib.GameLib.dto.UsuarioResponseDTO;
import com.gamelib.GameLib.exception.RegraNegocioException;
import com.gamelib.GameLib.model.Usuario;
import com.gamelib.GameLib.repository.UsuarioRepository;
import com.gamelib.GameLib.security.TokenService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
      TokenService tokenService) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
  }

  @Transactional
  public UsuarioResponseDTO cadastrarUsuario(UsuarioCadastroDTO dto) {
    if (usuarioRepository.existsByEmail(dto.email())) {
      throw new RegraNegocioException("Email já cadastrado");
    }

    Usuario usuario = new Usuario();
    usuario.setNome(dto.nome());
    usuario.setEmail(dto.email());
    usuario.setSenha(passwordEncoder.encode(dto.senha()));

    Usuario salvo = usuarioRepository.save(usuario);
    return UsuarioResponseDTO.fromEntity(salvo);
  }

  @Transactional
  public TokenResponseDTO autenticar(LoginDTO dto) {
    Usuario usuario = usuarioRepository.findByEmail(dto.email())
        .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas."));

    if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
      throw new RegraNegocioException("Credenciais inválidas.");
    }

    String token = tokenService.gerarToken(usuario);
    return new TokenResponseDTO(token, usuario.getId(), usuario.getNome());
  }

  @Transactional
  public Usuario buscarEntityPorId(Long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
  }

  @Transactional
  public UsuarioResponseDTO buscarPorId(Long id) {
    return usuarioRepository.findById(id)
        .map(UsuarioResponseDTO::fromEntity)
        .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
  }

}
