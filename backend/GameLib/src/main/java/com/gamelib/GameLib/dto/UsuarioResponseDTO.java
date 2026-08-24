package com.gamelib.GameLib.dto;

import java.time.LocalDateTime;

import com.gamelib.GameLib.model.Usuario;

public record UsuarioResponseDTO(
		Long id,
		String nome,
		String email,
		LocalDateTime dataCriacao) {
	public static UsuarioResponseDTO fromEntity(Usuario usuario) {
		return new UsuarioResponseDTO(
			usuario.getId(),
			usuario.getNome(),
			usuario.getEmail(),
			usuario.getDataCriacao());
	}
}
