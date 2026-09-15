package com.gamelib.GameLib.service;

import com.gamelib.GameLib.dto.external.RawgGameResponseDTO;
import com.gamelib.GameLib.dto.external.RawgSearchResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawgApiServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private RawgApiService rawgApiService;

    @Test
    @DisplayName("Deve buscar jogos com sucesso simulando o RestClient")
    void buscarJogosPorNomeSucesso() {
        ReflectionTestUtils.setField(rawgApiService, "apiUrl", "https://api.rawg.io/api");
        ReflectionTestUtils.setField(rawgApiService, "apiKey", "test-key");

        var game = new RawgGameResponseDTO(1L, "Cyberpunk 2077", "http://capa.jpg");
        var searchResponse = new RawgSearchResponseDTO(List.of(game));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RawgSearchResponseDTO.class)).thenReturn(searchResponse);

        List<RawgGameResponseDTO> resultado = rawgApiService.buscarJogosPorNome("Cyberpunk");

        assertFalse(resultado.isEmpty());
        assertEquals("Cyberpunk 2077", resultado.get(0).name());
        verify(requestHeadersUriSpec).uri(
                eq("https://api.rawg.io/api/games?key={key}&search={search}&page_size=10"),
                eq("test-key"),
                eq("Cyberpunk"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando a busca vier vazia")
    void buscarJogosPorNomeComTermoVazioRetornaListaVazia() {
        List<RawgGameResponseDTO> resultado = rawgApiService.buscarJogosPorNome("   ");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verifyNoInteractions(restClient);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando a API responder sem resultados")
    void buscarJogosPorNomeSemResultadosRetornaListaVazia() {
        ReflectionTestUtils.setField(rawgApiService, "apiUrl", "https://api.rawg.io/api");
        ReflectionTestUtils.setField(rawgApiService, "apiKey", "test-key");

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RawgSearchResponseDTO.class)).thenReturn(new RawgSearchResponseDTO(null));

        List<RawgGameResponseDTO> resultado = rawgApiService.buscarJogosPorNome("Portal");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando a API responder nula")
    void buscarJogosPorNomeRespostaNulaRetornaListaVazia() {
        ReflectionTestUtils.setField(rawgApiService, "apiUrl", "https://api.rawg.io/api");
        ReflectionTestUtils.setField(rawgApiService, "apiKey", "test-key");

        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RawgSearchResponseDTO.class)).thenReturn(null);

        List<RawgGameResponseDTO> resultado = rawgApiService.buscarJogosPorNome("Portal");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

}