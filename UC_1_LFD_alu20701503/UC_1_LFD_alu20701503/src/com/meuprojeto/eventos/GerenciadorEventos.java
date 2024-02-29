package com.meuprojeto.eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorEventos {
    private List<Evento> eventos;

    public GerenciadorEventos() {
        this.eventos = new ArrayList<>();
    }

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
    }

    public void removerEvento(Evento evento) {
        eventos.remove(evento);
    }

    public List<Evento> buscarEventosPorCategoria(CategoriaEvento categoria) {
        return eventos.stream()
                .filter(evento -> evento.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    public Evento buscarEventoPorNome(String nome) {
        return eventos.stream()
                .filter(evento -> evento.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    // Método para retornar todos os eventos
    public List<Evento> getTodosEventos() {
        return eventos;
    }

    public List<Evento> getEventosInscritosUsuario(String emailUsuario) {
        return eventos.stream()
                .filter(evento -> evento.getParticipantes().stream()
                        .anyMatch(participante -> participante.getEmail().equalsIgnoreCase(emailUsuario)))
                .collect(Collectors.toList());
    }

    // Implementações adicionais conforme necessário
}
