package com.gamelib.GameLib.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelib.GameLib.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  // Busca usuário por e-mail para autenticação
  Optional<Usuario> findByEmail(String email);

  // Verifica se o e-mail já está cadastrado no sistema
  boolean existsByEmail(String email);
}
