package com.gamelib.GameLib.service;

import org.springframework.stereotype.Service;

import com.gamelib.GameLib.dto.UsuarioCadastroDTO;
import com.gamelib.GameLib.dto.UsuarioResponseDTO;
import com.gamelib.GameLib.exception.RegraNegocioException;
import com.gamelib.GameLib.model.Usuario;
import com.gamelib.GameLib.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
  
  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Transactional
  public UsuarioResponseDTO cadastrarUsuario(UsuarioCadastroDTO dto) {
    if (usuarioRepository.existsByEmail(dto.email())) {
      throw new RegraNegocioException("Email já cadastrado");
    }

    Usuario usuario = new Usuario();
    usuario.setNome(dto.nome());
    usuario.setEmail(dto.email());
    usuario.setSenha(dto.senha());

    Usuario salvo = usuarioRepository.save(usuario);
    return UsuarioResponseDTO.fromEntity(salvo);
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
