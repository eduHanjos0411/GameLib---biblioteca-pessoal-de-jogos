package com.gamelib.GameLib.model;

import java.time.LocalDate;
import java.util.Objects;

import com.gamelib.GameLib.enums.StatusJogo;

import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "biblioteca_jogos")
public class BibliotecaJogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_jogo", nullable = false)
    private Jogo jogo;

    @NotBlank(message = "A plataforma é obrigatória")
    @Column(nullable = false)
    private String plataforma;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_andamento", nullable = false)
    private StatusJogo statusAndamento;

    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 10, message = "A nota máxima é 10")
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String opiniao;

    @Column(name = "data_adicao", nullable = false, updatable = false)
    private LocalDate dataAdicao;

    public BibliotecaJogo() {
    }

    @PrePersist
    protected void onCreate() {
        this.dataAdicao = LocalDate.now();
        if (this.statusAndamento == null) {
            this.statusAndamento = StatusJogo.NAO_INICIADO;
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public StatusJogo getStatusAndamento() { return statusAndamento; }
    public void setStatusAndamento(StatusJogo statusAndamento) { this.statusAndamento = statusAndamento; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }

    public String getOpiniao() { return opiniao; }
    public void setOpiniao(String opiniao) { this.opiniao = opiniao; }

    public LocalDate getDataAdicao() { return dataAdicao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BibliotecaJogo que = (BibliotecaJogo) o;
        return Objects.equals(id, que.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
