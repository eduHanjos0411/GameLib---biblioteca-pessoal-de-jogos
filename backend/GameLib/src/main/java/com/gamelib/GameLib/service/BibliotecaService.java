package com.gamelib.GameLib.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamelib.GameLib.dto.AdicionarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.AtualizarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.BibliotecaJogoResponseDTO;
import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.exception.RegraNegocioException;
import com.gamelib.GameLib.exception.ResourceNotFoundException;
import com.gamelib.GameLib.model.BibliotecaJogo;
import com.gamelib.GameLib.model.Jogo;
import com.gamelib.GameLib.model.Usuario;
import com.gamelib.GameLib.repository.BibliotecaJogoRepository;
import com.gamelib.GameLib.repository.JogoRepository;

import jakarta.transaction.Transactional;

@Service
public class BibliotecaService {

  private final BibliotecaJogoRepository bibliotecaJogoRepository;
  private final JogoRepository jogoRepository;
  private final UsuarioService usuarioService;

  public BibliotecaService(BibliotecaJogoRepository bibliotecaJogoRepository, JogoRepository jogoRepository,
      UsuarioService usuarioService) {
    this.bibliotecaJogoRepository = bibliotecaJogoRepository;
    this.jogoRepository = jogoRepository;
    this.usuarioService = usuarioService;
  }

  @Transactional
  public BibliotecaJogoResponseDTO adicionarJogo(Long usuarioId, AdicionarJogoBibliotecaDTO dto) {
    Usuario usuario = usuarioService.buscarEntityPorId(usuarioId);

    Jogo jogo = jogoRepository.findByTituloIgnoreCase(dto.titulo()).orElseGet(() -> {
        Jogo novoJogo = new Jogo();
        novoJogo.setTitulo(dto.titulo());
        novoJogo.setCategoria(dto.categoria());
        novoJogo.setUrlCapa(dto.urlCapa());
        novoJogo.setApiExternalId(dto.apiExternalId());
        return jogoRepository.save(novoJogo);
      });

      boolean jaExiste = bibliotecaJogoRepository.existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(usuario.getId(), jogo.getId(), dto.plataforma());

      if (jaExiste) {
            throw new RegraNegocioException("Este jogo já está cadastrado em sua biblioteca para a plataforma " + dto.plataforma());
        }

        BibliotecaJogo itemBiblioteca = new BibliotecaJogo();
        itemBiblioteca.setUsuario(usuario);
        itemBiblioteca.setJogo(jogo);
        itemBiblioteca.setPlataforma(dto.plataforma());
        itemBiblioteca.setStatusJogo(dto.statusJogo() != null ? dto.statusJogo() : StatusJogo.NAO_INICIADO);
        itemBiblioteca.setNota(dto.nota());
        itemBiblioteca.setOpiniao(dto.opiniao());

        BibliotecaJogo salvo = bibliotecaJogoRepository.save(itemBiblioteca);
        return BibliotecaJogoResponseDTO.fromEntity(salvo);
  }

  @Transactional
  public List<BibliotecaJogoResponseDTO> listarBibliotecaPorUsuario(Long usuarioId) {
    usuarioService.buscarEntityPorId(usuarioId);
    return bibliotecaJogoRepository.findByUsuarioId(usuarioId)
    .stream().map(BibliotecaJogoResponseDTO::fromEntity).toList();
  }

  @Transactional
  public List<BibliotecaJogoResponseDTO> listarPorStatus(Long usuarioId, StatusJogo status) {
    return bibliotecaJogoRepository.findByUsuarioIdAndStatusJogo(usuarioId, status)
    .stream().map(BibliotecaJogoResponseDTO::fromEntity).toList();
  }

  @Transactional
  public BibliotecaJogoResponseDTO atualizarItem(Long usuarioId, Long itemBibliotecaId, AtualizarJogoBibliotecaDTO dto) {
    BibliotecaJogo item = bibliotecaJogoRepository.findByIdAndUsuarioId(itemBibliotecaId, usuarioId)
      .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado ou você não tem permissão para alterá-lo."));

    item.setPlataforma(dto.plataforma());
    if (dto.statusJogo() != null) {
      item.setStatusJogo(dto.statusJogo());
    }
    item.setNota(dto.nota());
    item.setOpiniao(dto.opiniao());

    BibliotecaJogo atualizado = bibliotecaJogoRepository.save(item);
    return BibliotecaJogoResponseDTO.fromEntity(atualizado);
  }

  @Transactional
    public void removerItem(Long usuarioId, Long itemBibliotecaId) {
        BibliotecaJogo item = bibliotecaJogoRepository.findByIdAndUsuarioId(itemBibliotecaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado ou você não tem permissão para removê-lo."));

        bibliotecaJogoRepository.delete(item);
    }

}
