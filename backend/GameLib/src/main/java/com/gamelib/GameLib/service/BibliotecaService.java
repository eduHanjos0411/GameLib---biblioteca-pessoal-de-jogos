package com.gamelib.GameLib.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamelib.GameLib.dto.AdicionarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.AtualizarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.BibliotecaJogoResponseDTO;
import com.gamelib.GameLib.dto.external.RawgGameResponseDTO;
import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.exception.RegraNegocioException;
import com.gamelib.GameLib.exception.ResourceNotFoundException;
import com.gamelib.GameLib.model.BibliotecaJogo;
import com.gamelib.GameLib.model.Jogo;
import com.gamelib.GameLib.model.Usuario;
import com.gamelib.GameLib.repository.BibliotecaJogoRepository;
import com.gamelib.GameLib.repository.JogoRepository;

import java.util.List;

@Service
public class BibliotecaService {

  private final BibliotecaJogoRepository bibliotecaJogoRepository;
  private final JogoRepository jogoRepository;
  private final UsuarioService usuarioService;
  private final RawgApiService rawgApiService;

  public BibliotecaService(BibliotecaJogoRepository bibliotecaJogoRepository,
      JogoRepository jogoRepository,
      UsuarioService usuarioService,
      RawgApiService rawgApiService) {
    this.bibliotecaJogoRepository = bibliotecaJogoRepository;
    this.jogoRepository = jogoRepository;
    this.usuarioService = usuarioService;
    this.rawgApiService = rawgApiService;
  }

  @Transactional
  public BibliotecaJogoResponseDTO adicionarJogo(Long usuarioId, AdicionarJogoBibliotecaDTO dto) {
    Usuario usuario = usuarioService.buscarEntityPorId(usuarioId);

    // 1. Procura o jogo no banco local (por ID externo ou Título)
    Jogo jogo = resolverOuCriarJogo(dto);

    // 2. Valida se o usuário já possui este jogo na mesma plataforma
    boolean jaExiste = bibliotecaJogoRepository
        .existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(usuario.getId(), jogo.getId(), dto.plataforma());

    if (jaExiste) {
      throw new RegraNegocioException(
          "Este jogo já está cadastrado em sua biblioteca para a plataforma " + dto.plataforma());
    }

    // 3. Cria o vínculo na biblioteca do usuário
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

  private Jogo resolverOuCriarJogo(AdicionarJogoBibliotecaDTO dto) {
    // Tenta buscar por ID da API externa caso informado
    if (dto.apiExternalId() != null && !dto.apiExternalId().isBlank()) {
      var jogoExistente = jogoRepository.findByApiExternalId(dto.apiExternalId());
      if (jogoExistente.isPresent()) {
        return jogoExistente.get();
      }
    }

    // Tenta buscar pelo título no catálogo local
    return jogoRepository.findByTituloIgnoreCase(dto.titulo())
        .orElseGet(() -> criarNovoJogoComDadosExternos(dto));
  }

  private Jogo criarNovoJogoComDadosExternos(AdicionarJogoBibliotecaDTO dto) {
    Jogo novoJogo = new Jogo();
    novoJogo.setTitulo(dto.titulo());
    novoJogo.setCategoria(dto.categoria());
    novoJogo.setUrlCapa(dto.urlCapa());
    novoJogo.setApiExternalId(dto.apiExternalId());

    // Se dados de capa/categoria vierem vazios do DTO, busca do RAWG como fallback
    if ((dto.urlCapa() == null || dto.categoria() == null) && dto.titulo() != null) {
      List<RawgGameResponseDTO> buscaExterna = rawgApiService.buscarJogosPorNome(dto.titulo());
      if (!buscaExterna.isEmpty()) {
        RawgGameResponseDTO dadosApi = buscaExterna.get(0);
        if (novoJogo.getUrlCapa() == null) {
          novoJogo.setUrlCapa(dadosApi.backgroundImage());
        }
        if (novoJogo.getCategoria() == null && dadosApi.genres() != null && !dadosApi.genres().isEmpty()) {
          novoJogo.setCategoria(dadosApi.genres().get(0).name());
        }
        if (novoJogo.getApiExternalId() == null && dadosApi.id() != null) {
          novoJogo.setApiExternalId(dadosApi.id().toString());
        }
      }
    }

    return jogoRepository.save(novoJogo);
  }

  @Transactional(readOnly = true)
  public List<BibliotecaJogoResponseDTO> listarBibliotecaPorUsuario(Long usuarioId) {
    usuarioService.buscarEntityPorId(usuarioId);
    return bibliotecaJogoRepository.findByUsuarioId(usuarioId)
        .stream()
        .map(BibliotecaJogoResponseDTO::fromEntity)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<BibliotecaJogoResponseDTO> listarPorStatus(Long usuarioId, StatusJogo status) {
    return bibliotecaJogoRepository.findByUsuarioIdAndStatusJogo(usuarioId, status)
        .stream()
        .map(BibliotecaJogoResponseDTO::fromEntity)
        .toList();
  }

  @Transactional
  public BibliotecaJogoResponseDTO atualizarItem(Long usuarioId, Long itemBibliotecaId,
      AtualizarJogoBibliotecaDTO dto) {
    BibliotecaJogo item = bibliotecaJogoRepository.findByIdAndUsuarioId(itemBibliotecaId, usuarioId)
        .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado ou sem permissão para alteração."));

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
        .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado ou sem permissão para remoção."));

    bibliotecaJogoRepository.delete(item);
  }
}