package com.gamelib.GameLib.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O nome é obrigatório")
  @Column(nullable = false)
  private String nome;
  
  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Formato de email inválido")
  @Column(nullable = false)
  private String email;

  @NotBlank(message = "A senha é obrigatória")
  @Column(name = "senha_hash", nullable = false)
  private String senha;

  @Column(name = "data_criacao", nullable = false)
  private LocalDateTime dataCriacao;

  public Usuario() {
  }

  public Usuario(String nome, String email, String senha) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
  }

  @PrePersist
  protected void onCreate() {
    this.dataCriacao = LocalDateTime.now();
  }

  public Long getId() {return id;}
  public void setId(Long id) {this.id = id;}

  public String getNome() {return nome;}
  public void setNome(String nome) {this.nome = nome;}

  public String getEmail() {return email;}
  public void setEmail(String email) {this.email = email;}

  public String getSenha() {return senha;}
  public void setSenha(String senha) {this.senha = senha;}

  public LocalDateTime getDataCriacao() {return dataCriacao;}

  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
