package com.gamelib.GameLib.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamelib.GameLib.dto.AdicionarJogoBibliotecaDTO;
import com.gamelib.GameLib.dto.BibliotecaJogoResponseDTO;
import com.gamelib.GameLib.enums.StatusJogo;
import com.gamelib.GameLib.exception.RegraNegocioException;
import com.gamelib.GameLib.model.BibliotecaJogo;
import com.gamelib.GameLib.model.Jogo;
import com.gamelib.GameLib.model.Usuario;
import com.gamelib.GameLib.repository.BibliotecaJogoRepository;
import com.gamelib.GameLib.repository.JogoRepository;

@ExtendWith(MockitoExtension.class)
public class BibliotecaServiceTest {

  @Mock
  private BibliotecaJogoRepository bibliotecaJogoRepository;

  @Mock
  private JogoRepository jogoRepository;

  @Mock
  private UsuarioService usuarioService;

  @InjectMocks
  private BibliotecaService bibliotecaService;

  private Usuario usuario;
  private Jogo jogo;
  private BibliotecaJogo bibliotecaJogo;
  private AdicionarJogoBibliotecaDTO adicionarDTO;

  @BeforeEach
  void setUp() {
    usuario = new Usuario("Eduardo", "eduardo@email.com", "senha123");
    usuario.setId(1L);

    jogo = new Jogo("Cyberpunk 2077", "RPG", "http://capa.jpg", "12345");
    jogo.setId(10L);

    bibliotecaJogo = new BibliotecaJogo();
    bibliotecaJogo.setId(100L);
    bibliotecaJogo.setUsuario(usuario);
    bibliotecaJogo.setJogo(jogo);
    bibliotecaJogo.setPlataforma("PC");
    bibliotecaJogo.setStatusJogo(StatusJogo.JOGANDO);
    bibliotecaJogo.setNota(9);

    adicionarDTO = new AdicionarJogoBibliotecaDTO(
            "Cyberpunk 2077", "PC", StatusJogo.JOGANDO, 9,
            "Jogo excelente!", "RPG", "http://capa.jpg", "12345"
    );
  }

  @Test
  @DisplayName("Deve adicionar jogo na biblioteca com sucesso")
  void adicionarJogoComSucesso() {
    when(usuarioService.buscarEntityPorId(1L)).thenReturn(usuario);
    when(jogoRepository.findByTituloIgnoreCase("Cyberpunk 2077")).thenReturn(Optional.of(jogo));
    when(bibliotecaJogoRepository.existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(1L, 10L, "PC")).thenReturn(false);
    when(bibliotecaJogoRepository.save(any(BibliotecaJogo.class))).thenReturn(bibliotecaJogo);

    BibliotecaJogoResponseDTO response = bibliotecaService.adicionarJogo(1L, adicionarDTO);

    assertNotNull(response);
    assertEquals("Cyberpunk 2077", response.titulo());
    assertEquals("PC", response.plataforma());
    assertEquals(StatusJogo.JOGANDO, response.statusJogo());
    verify(bibliotecaJogoRepository, times(1)).save(any(BibliotecaJogo.class));
  }

  @Test
  @DisplayName("Deve lançar RegraNegocioException se o jogo já estiver cadastrado para a mesma plataforma")
  void adicionarJogoDuplicadoNaMesmaPlataformaLancaExcecao() {
    when(usuarioService.buscarEntityPorId(1L)).thenReturn(usuario);
    when(jogoRepository.findByTituloIgnoreCase("Cyberpunk 2077")).thenReturn(Optional.of(jogo));
    when(bibliotecaJogoRepository.existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(1L, 10L, "PC")).thenReturn(true);

    RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
        bibliotecaService.adicionarJogo(1L, adicionarDTO);
    });

    assertTrue(exception.getMessage().contains("já está cadastrado em sua biblioteca para a plataforma PC"));
    verify(bibliotecaJogoRepository, never()).save(any(BibliotecaJogo.class));
  }

  @Test
  @DisplayName("Deve remover item da biblioteca com sucesso")
  void removerItemSucesso() {
    when(bibliotecaJogoRepository.findByIdAndUsuarioId(100L, 1L)).thenReturn(Optional.of(bibliotecaJogo));
    doNothing().when(bibliotecaJogoRepository).delete(bibliotecaJogo);

    assertDoesNotThrow(() -> bibliotecaService.removerItem(1L, 100L));
    verify(bibliotecaJogoRepository, times(1)).delete(bibliotecaJogo);
  }
}
