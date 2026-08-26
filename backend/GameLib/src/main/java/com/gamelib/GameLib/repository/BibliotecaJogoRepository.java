package com.gamelib.GameLib.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.model.BibliotecaJogo;

@Repository
public interface BibliotecaJogoRepository extends JpaRepository<BibliotecaJogo, Long> {
  // Lista todos os jogos na biblioteca de um usuário específico
  List<BibliotecaJogo> findByUsuarioId(Long usuarioId);

  // Filtra os jogos de um usuário por status de andamento (ex: JOGANDO, ZERADO)
  List<BibliotecaJogo> findByUsuarioIdAndStatusJogo(Long usuarioId, StatusJogo status);

  // Filtra os jogos de um usuário por plataforma (ex: PC, PlayStation)
  List<BibliotecaJogo> findByUsuarioIdAndPlataformaIgnoreCase(Long usuarioId, String plataforma);

  // Busca um item específico garantindo que pertença ao usuário logado
  // (Segurança/Autorização)
  Optional<BibliotecaJogo> findByIdAndUsuarioId(Long id, Long usuarioId);

  // Verifica se um determinado jogo já foi adicionado na biblioteca de um usuário
  // para aquela plataforma
  boolean existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(Long usuarioId, Long jogoId, String plataforma);
}
