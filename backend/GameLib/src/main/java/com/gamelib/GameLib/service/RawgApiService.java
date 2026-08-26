package com.gamelib.GameLib.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.gamelib.GameLib.dto.external.RawgGameResponseDTO;
import com.gamelib.GameLib.dto.external.RawgSearchResponseDTO;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
public class RawgApiService {
  
  private final RestClient restClient;

  @Value("${rawg.api.url}")
  private String apiUrl;

  @Value("${rawg.api.key}")
  private String apiKey;

  public RawgApiService(RestClient restClient) {
        this.restClient = restClient;
    }

  public List<RawgGameResponseDTO> buscarJogosPorNome(String nome) {
      RawgSearchResponseDTO response = restClient.get()
              .uri(apiUrl + "/games?key={key}&search={search}&page_size=10", apiKey, nome)
              .retrieve()
              .body(RawgSearchResponseDTO.class);

      return response != null && response.results() != null ? response.results() : Collections.emptyList();
  }
}
