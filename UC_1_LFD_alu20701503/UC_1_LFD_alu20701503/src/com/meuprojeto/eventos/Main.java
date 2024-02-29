package com.meuprojeto.eventos;

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
        // Implementar listagem de eventos
    }

    private void adicionarEvento() {
        // Implementar adição de eventos
    }

    private void inscreverEvento() {
        // Implementar inscrição em eventos
    }

    private void cancelarInscricaoEvento() {
        // Implementar cancelamento de inscrição em eventos
    }

    public static void main(String[] args) {
        new Main().iniciar();
    }

    // Não esqueça de fechar o scanner quando o programa terminar
    // scanner.close();
}
