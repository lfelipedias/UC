package com.meuprojeto.eventos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Evento {
    private String nome;
    private String endereco;
    private CategoriaEvento categoria;
    private LocalDateTime dataHora;
    private String descricao;
    private Set<Usuario> participantes = new HashSet<>();

    public Evento(String nome, String endereco, CategoriaEvento categoria, LocalDateTime dataHora, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public CategoriaEvento getCategoria() {
        return categoria;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public Set<Usuario> getParticipantes() {
        return participantes;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setCategoria(CategoriaEvento categoria) {
        this.categoria = categoria;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Métodos para adicionar e remover participantes
    public void adicionarParticipante(Usuario usuario) {
        participantes.add(usuario);
    }

    public void removerParticipante(Usuario usuario) {
        participantes.remove(usuario);
    }

    // Outros métodos relacionados a eventos podem ser adicionados aqui.
}
