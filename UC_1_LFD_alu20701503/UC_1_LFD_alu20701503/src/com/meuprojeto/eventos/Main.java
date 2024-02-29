package com.meuprojeto.eventos;

import java.time.LocalDateTime;
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
            System.out.println("4 - Cancelar Inscrição em um Evento");
            System.out.println("5 - Sair");
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
                    cancelarInscricaoEvento();
                    break;
                case 5:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void listarEventos() {
        List<Evento> eventos = gerenciadorEventos.getTodosEventos();
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
                // Se quiser listar os participantes, descomente o seguinte código:
                // evento.getParticipantes().forEach(participante ->
                // System.out.println(participante.getNome()));
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

        System.out.print("Data e hora do evento (formato: YYYY-MM-DDTHH:MM): ");
        LocalDateTime dataHora = LocalDateTime.parse(scanner.nextLine());

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

        // Procura o evento pelo nome
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

        System.out.println("Inscrição realizada com sucesso!");
    }

    private void cancelarInscricaoEvento() {
        System.out.println("Cancelar inscrição de um evento:");
        listarEventos(); // Mostra a lista de eventos para o usuário

        System.out.print("Digite o nome do evento do qual deseja cancelar a inscrição: ");
        String nomeEvento = scanner.nextLine();

        // Procura o evento pelo nome
        Evento evento = gerenciadorEventos.buscarEventoPorNome(nomeEvento);
        if (evento == null) {
            System.out.println("Evento não encontrado.");
            return;
        }

        System.out.print("Digite seu email: ");
        String emailUsuario = scanner.nextLine();

        // Procura o usuário pelo email entre os participantes do evento
        Usuario usuario = evento.getParticipantes().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(emailUsuario))
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            System.out.println("Usuário não encontrado na lista de participantes do evento.");
            return;
        }

        // Remove o usuário da lista de participantes do evento
        evento.removerParticipante(usuario);
        System.out.println("Inscrição cancelada com sucesso!");
    }

    public static void main(String[] args) {
        new Main().iniciar();
    }

    // Não esqueça de fechar o scanner quando o programa terminar
    // scanner.close();
}
