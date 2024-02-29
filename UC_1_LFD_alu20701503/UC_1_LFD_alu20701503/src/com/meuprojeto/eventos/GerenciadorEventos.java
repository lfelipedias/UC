package com.meuprojeto.eventos;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class GerenciadorEventos {
    private static final String EVENTS_FILE = "events.data";
    private static final String VINCULOS_FILE = "vinculos_eventos_usuarios.txt";
    private List<Evento> eventos;
    private List<VinculoEventoUsuario> vinculos;

    public GerenciadorEventos() {
        this.eventos = new ArrayList<>();
        this.vinculos = new ArrayList<>();
        carregarEventos();
        carregarVinculos();
    }

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
        salvarEventos();
    }

    public void removerEvento(Evento evento) {
        eventos.remove(evento);
        salvarEventos();
        removerVinculosPorEvento(evento.getNome());
    }

    private void salvarEventos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTS_FILE))) {
            for (Evento evento : eventos) {
                writer.write(eventoParaString(evento));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos em arquivo: " + e.getMessage());
        }
    }

    private void carregarEventos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                eventos.add(stringParaEvento(linha));
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos do arquivo: " + e.getMessage());
        }
    }

    private Evento stringParaEvento(String linha) {
        String[] partes = linha.split(";");
        String nome = partes[0].substring(partes[0].indexOf(":") + 1);
        String endereco = partes[1].substring(partes[1].indexOf(":") + 1);
        CategoriaEvento categoria = CategoriaEvento.valueOf(partes[2].substring(partes[2].indexOf(":") + 1));
        LocalDateTime dataHora = LocalDateTime.parse(partes[3].substring(partes[3].indexOf(":") + 1));
        String descricao = partes[4].substring(partes[4].indexOf(":") + 1);
        return new Evento(nome, endereco, categoria, dataHora, descricao);
    }

    private String eventoParaString(Evento evento) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome:").append(evento.getNome()).append(";");
        sb.append("Endereço:").append(evento.getEndereco()).append(";");
        sb.append("Categoria:").append(evento.getCategoria()).append(";");
        sb.append("DataHora:").append(evento.getDataHora()).append(";");
        sb.append("Descrição:").append(evento.getDescricao());
        return sb.toString();
    }

    // Método para listar todos os eventos, ordenados por data e hora
    public List<Evento> getEventosOrdenados() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getDataHora))
                .collect(Collectors.toList());
    }

    // Método para verificar se um evento está ocorrendo no momento
    public List<Evento> getEventosOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return eventos.stream()
                .filter(evento -> evento.getDataHora().isBefore(agora) || evento.getDataHora().isEqual(agora))
                .collect(Collectors.toList());
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

    public List<Evento> getTodosEventos() {
        return eventos;
    }

    public List<Evento> getEventosInscritosUsuario(String emailUsuario) {
        List<Evento> eventosInscritos = new ArrayList<>();

        for (VinculoEventoUsuario vinculo : vinculos) {
            if (vinculo.getEmailUsuario().equalsIgnoreCase(emailUsuario)) {
                Evento evento = buscarEventoPorNome(vinculo.getNomeEvento());
                if (evento != null) {
                    eventosInscritos.add(evento);
                }
            }
        }

        return eventosInscritos;
    }

    public void salvarVinculoEventoUsuario(String nomeEvento, String emailUsuario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VINCULOS_FILE, true))) {
            writer.write(nomeEvento + "," + emailUsuario);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar o vínculo entre evento e usuário em arquivo: " + e.getMessage());
        } finally {
            // Atualiza a lista de vínculos após adicionar um novo vínculo para garantir que a memória esteja sincronizada com o arquivo
            carregarVinculos();
        }
    }
        
    public void removerVinculoEventoUsuario(String nomeEvento, String emailUsuario) {
        vinculos.removeIf(vinculo -> vinculo.getNomeEvento().equals(nomeEvento) && vinculo.getEmailUsuario().equals(emailUsuario));
        salvarVinculos();
    }

    private void carregarVinculos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(VINCULOS_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                String nomeEvento = partes[0];
                String emailUsuario = partes[1];
                vinculos.add(new VinculoEventoUsuario(nomeEvento, emailUsuario));
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar os vínculos entre eventos e usuários do arquivo: " + e.getMessage());
        }
    }

    private void salvarVinculos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VINCULOS_FILE))) {
            for (VinculoEventoUsuario vinculo : vinculos) {
                writer.write(vinculo.getNomeEvento() + "," + vinculo.getEmailUsuario());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar vínculos em arquivo: " + e.getMessage());
        }
    }

    private void removerVinculosPorEvento(String nomeEvento) {
        vinculos.removeIf(vinculo -> vinculo.getNomeEvento().equals(nomeEvento));
        salvarVinculos();
    }

    public class VinculoEventoUsuario {
        private String nomeEvento;
        private String emailUsuario;

        public VinculoEventoUsuario(String nomeEvento, String emailUsuario) {
            this.nomeEvento = nomeEvento;
            this.emailUsuario = emailUsuario;
        }

        public String getNomeEvento() {
            return nomeEvento;
        }

        public String getEmailUsuario() {
            return emailUsuario;
        }
    }

    public class Usuario {
        @SuppressWarnings("unused")
        private String email;

        public Usuario(String email) {
            this.email = email;
        }

        // Getters e setters (se necessário)
    }
}
