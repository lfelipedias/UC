package com.meuprojeto.eventos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private GerenciadorEventos gerenciadorEventos;
    private Scanner scanner;

    public Main() {
        this.gerenciadorEventos = new GerenciadorEventos();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean sair = false;
        while (!sair) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Listar Eventos");
            System.out.println("2 - Adicionar Evento");
            System.out.println("3 - Inscrever-se em um Evento");
            System.out.println("4 - Visualizar eventos inscritos");
            System.out.println("5 - Cancelar Inscrição em um Evento");
            System.out.println("6 - Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    listarEventos();
                    break;
                case 2:
                    adicionarEvento();
                    break;
                case 3:
                    inscreverEvento();
                    break;
                case 4:
                    mostrarEventosInscritos();
                    break;
                case 5:
                    cancelarInscricaoEvento();
                    break;
                case 6:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void listarEventos() {
        List<Evento> eventos = new ArrayList<>(gerenciadorEventos.getTodosEventos());
        eventos.sort(Comparator.comparing(Evento::getDataHora)); // Ordena eventos por data e hora

        LocalDateTime agora = LocalDateTime.now();

        if (eventos.isEmpty()) {
            System.out.println("Não há eventos cadastrados no momento.");
        } else {
            System.out.println("Eventos cadastrados:");
            for (Evento evento : eventos) {
                System.out.println("--------------------------------------------------");
                System.out.println("Nome: " + evento.getNome());
                System.out.println("Endereço: " + evento.getEndereco());
                System.out.println("Categoria: " + evento.getCategoria().toString());
                System.out.println("Data e Hora: " + evento.getDataHora().toString());
                System.out.println("Descrição: " + evento.getDescricao());
                System.out.println("Participantes: " + evento.getParticipantes().size());

                // Verifica se o evento está ocorrendo agora ou ocorreu nas últimas duas horas
                if (!evento.getDataHora().isAfter(agora) && 
                    evento.getDataHora().isAfter(agora.minusHours(2))) {
                    System.out.println("Este evento está ocorrendo AGORA ou ocorreu recentemente!");
                }
            }
            System.out.println("--------------------------------------------------");
        }
    }

    private void adicionarEvento() {
        System.out.println("Adicionar novo evento:");

        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine();

        System.out.print("Endereço do evento: ");
        String endereco = scanner.nextLine();

        System.out.println("Categoria do evento: ");
        for (CategoriaEvento cat : CategoriaEvento.values()) {
            System.out.println(cat.ordinal() + 1 + " - " + cat.name());
        }
        System.out.print("Escolha uma categoria (número): ");
        int categoriaIndex = Integer.parseInt(scanner.nextLine()) - 1;
        CategoriaEvento categoria = CategoriaEvento.values()[categoriaIndex];

        System.out.print("Data e hora do evento (formato: YYYY-MM-DD HH:MM): ");
        String dataHoraStr = scanner.nextLine();

        // Cria um DateTimeFormatter com o padrão esperado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Converte a string de entrada para um LocalDateTime usando o formatador
        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(dataHoraStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("A data e hora devem estar no formato 'YYYY-MM-DD HH:MM'.");
            return; // Sai do método se a data e hora estiverem no formato incorreto
        }

        System.out.print("Descrição do evento: ");
        String descricao = scanner.nextLine();

        Evento evento = new Evento(nome, endereco, categoria, dataHora, descricao);
        gerenciadorEventos.adicionarEvento(evento);
        System.out.println("Evento adicionado com sucesso!");
    }

    private void inscreverEvento() {
        System.out.println("Inscrever-se em um evento:");
        listarEventos(); // Mostra a lista de eventos para o usuário
    
        System.out.print("Digite o nome do evento em que deseja se inscrever: ");
        String nomeEvento = scanner.nextLine();
    
        Evento evento = gerenciadorEventos.buscarEventoPorNome(nomeEvento);
        if (evento == null) {
            System.out.println("Evento não encontrado.");
            return;
        }
    
        System.out.println("Inscrevendo no evento: " + evento.getNome());
        System.out.print("Digite seu nome: ");
        String nomeUsuario = scanner.nextLine();
    
        System.out.print("Digite seu email: ");
        String emailUsuario = scanner.nextLine();
    
        System.out.print("Digite sua cidade: ");
        String cidadeUsuario = scanner.nextLine();
    
        Usuario usuario = new Usuario(nomeUsuario, emailUsuario, cidadeUsuario);
        evento.adicionarParticipante(usuario); // Adiciona o usuário à lista de participantes
    
        gerenciadorEventos.salvarVinculoEventoUsuario(nomeEvento, emailUsuario); // Salva o vínculo em um arquivo
    
        System.out.println("Inscrição realizada com sucesso!");
    }
    
    private void cancelarInscricaoEvento() {
        System.out.println("Cancelar inscrição de um evento:");
        listarEventos(); // Mostra a lista de eventos para o usuário
    
        System.out.print("Digite o nome do evento do qual deseja cancelar a inscrição: ");
        String nomeEvento = scanner.nextLine();
    
        Evento evento = gerenciadorEventos.buscarEventoPorNome(nomeEvento);
        if (evento == null) {
            System.out.println("Evento não encontrado.");
            return;
        }
    
        System.out.print("Digite seu email: ");
        String emailUsuario = scanner.nextLine();
    
        // A verificação e remoção do participante são feitas dentro de GerenciadorEventos
        gerenciadorEventos.removerVinculoEventoUsuario(nomeEvento, emailUsuario); // Remove o vínculo do arquivo
    
        System.out.println("Inscrição cancelada com sucesso!");
    }

    private void mostrarEventosInscritos() {
        System.out.print("Digite seu email para ver os eventos inscritos: ");
        String emailUsuario = scanner.nextLine();

        List<Evento> eventosInscritos = gerenciadorEventos.getEventosInscritosUsuario(emailUsuario);
        if (eventosInscritos.isEmpty()) {
            System.out.println("Você não está inscrito em nenhum evento.");
        } else {
            System.out.println("Você está inscrito nos seguintes eventos:");
            for (Evento evento : eventosInscritos) {
                System.out.println("--------------------------------------------------");
                System.out.println("Nome: " + evento.getNome());
                System.out.println("Data e Hora: " + evento.getDataHora());
                // Exibir mais detalhes conforme necessário
            }
        }
    }

    public static void main(String[] args) {
        new Main().iniciar();
    }

    // Não esqueça de fechar o scanner quando o programa terminar
    //scanner.close();
}
