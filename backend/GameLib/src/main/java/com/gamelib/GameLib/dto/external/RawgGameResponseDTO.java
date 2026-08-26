package com.gamelib.GameLib.dto.external;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgGameResponseDTO(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("background_image") String backgroundImage,
    @JsonProperty("genres") List<GenreDTO> genres) {
  public record GenreDTO(
      @JsonProperty("name") String name) {
  }
}
