package com.gamelib.GameLib.model;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "jogos")
public class Jogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    private String categoria;

    @Column(name = "url_capa")
    private String urlCapa;

    @Column(name = "api_external_id")
    private String apiExternalId;

    public Jogo() {
    }

    public Jogo(String titulo, String categoria, String urlCapa, String apiExternalId) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.urlCapa = urlCapa;
        this.apiExternalId = apiExternalId;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getUrlCapa() { return urlCapa; }
    public void setUrlCapa(String urlCapa) { this.urlCapa = urlCapa; }

    public String getApiExternalId() { return apiExternalId; }
    public void setApiExternalId(String apiExternalId) { this.apiExternalId = apiExternalId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jogo jogo = (Jogo) o;
        return Objects.equals(id, jogo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
