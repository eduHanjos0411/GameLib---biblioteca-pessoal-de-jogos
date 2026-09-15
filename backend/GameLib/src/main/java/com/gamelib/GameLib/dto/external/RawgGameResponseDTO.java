package com.gamelib.GameLib.dto.external;


import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgGameResponseDTO(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("background_image") String background_image) {

  }

