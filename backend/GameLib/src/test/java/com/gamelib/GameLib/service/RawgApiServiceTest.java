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
        // Configura as propriedades anotadas com @Value na Service
        ReflectionTestUtils.setField(rawgApiService, "apiUrl", "https://api.rawg.io/api");
        ReflectionTestUtils.setField(rawgApiService, "apiKey", "test-key");

        // Dado simulado
        var game = new RawgGameResponseDTO(1L, "Cyberpunk 2077", "http://capa.jpg", List.of());
        var searchResponse = new RawgSearchResponseDTO(List.of(game));

        // Mocks encadeados do RestClient
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RawgSearchResponseDTO.class)).thenReturn(searchResponse);

        // Execução
        List<RawgGameResponseDTO> resultado = rawgApiService.buscarJogosPorNome("Cyberpunk");

        // Asserções
        assertFalse(resultado.isEmpty());
        assertEquals("Cyberpunk 2077", resultado.get(0).name());
    }
}