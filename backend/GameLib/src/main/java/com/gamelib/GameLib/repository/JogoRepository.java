package com.gamelib.GameLib.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelib.GameLib.model.Jogo;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
  // Busca parcial por título (case insensitive) para busca/autocompletar
  List<Jogo> findByTituloContainingIgnoreCase(String titulo);

  // Busca por ID da API externa caso o jogo tenha vindo do RAWG/IGDB
  Optional<Jogo> findByApiExternalId(String apiExternalId);

  // Verifica se um jogo com título exato já existe no catálogo
  Optional<Jogo> findByTituloIgnoreCase(String titulo);
}
