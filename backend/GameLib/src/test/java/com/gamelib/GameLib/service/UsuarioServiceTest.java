package com.gamelib.GameLib.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.gamelib.GameLib.dto.UsuarioCadastroDTO;
import com.gamelib.GameLib.dto.UsuarioResponseDTO;
import com.gamelib.GameLib.exception.RegraNegocioException;
import com.gamelib.GameLib.model.Usuario;
import com.gamelib.GameLib.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @Mock
  private RawgApiService rawgApiService;

  @InjectMocks
  private UsuarioService usuarioService;

  private Usuario usuario;
  private UsuarioCadastroDTO cadastroDTO;

  @BeforeEach
  void setUp() {
    usuario = new Usuario("Pedro", "pedro@email.com", "senha123");
    usuario.setId(1L);

    cadastroDTO = new UsuarioCadastroDTO("Pedro", "pedro@email.com", "senha123");
  }

  @Test
  @DisplayName("Deve cadastrar um novo usuário com sucesso quando o email for único")
  void testCadastrarUsuarioComSucesso() {
    when(usuarioRepository.existsByEmail(cadastroDTO.email())).thenReturn(false);
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    UsuarioResponseDTO response = usuarioService.cadastrarUsuario(cadastroDTO);

    assertNotNull(response);
    assertEquals(1L, response.id());
    assertEquals("Pedro", response.nome());
    assertEquals("pedro@email.com", response.email());
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
  }

  @Test
  @DisplayName("Deve lançar RegraNegocioException ao tentar cadastrar e-mail duplicado")
  void cadastrarUsuarioEmailDuplicadoLancaExcecao() {
    when(usuarioRepository.existsByEmail(cadastroDTO.email())).thenReturn(true);

    RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
      usuarioService.cadastrarUsuario(cadastroDTO);
    });

    assertEquals("Email já cadastrado", exception.getMessage());
    verify(usuarioRepository, never()).save(any(Usuario.class));
  }

  @Test
    @DisplayName("Deve buscar usuário por ID existente")
    void buscarPorIdSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID do usuário não existir")
    void buscarPorIdInexistenteLancaExcecao() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> {
            usuarioService.buscarPorId(99L);
        });
    }
}
