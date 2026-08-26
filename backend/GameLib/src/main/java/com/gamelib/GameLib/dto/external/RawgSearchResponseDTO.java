package com.gamelib.GameLib.dto.external;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgSearchResponseDTO(
  @JsonProperty("results") List<RawgGameResponseDTO> results
) {

}
