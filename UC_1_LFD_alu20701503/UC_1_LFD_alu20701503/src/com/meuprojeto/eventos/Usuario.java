package com.meuprojeto.eventos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Usuario {
    private String nome;
    private String email;
    private String cidade;

    // Construtor
    public Usuario(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    // Método equals sobrescrito para comparação baseada em email
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(email, usuario.email);
    }

    // Método hashCode sobrescrito baseado em email
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    // Método para salvar o usuário em um arquivo .txt
    public void salvarUsuario() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            writer.write(nome + "," + email + "," + cidade);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar o usuário em arquivo: " + e.getMessage());
        }
    }
}
