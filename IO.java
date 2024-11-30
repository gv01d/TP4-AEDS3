import java.util.Scanner;

import aed3.ListaInvertida;

public class IO {

    public static void main(String[] args) {

        try {
            StopWords stopWords = new StopWords("dicionario.stopWords");

            ArquivoCategorias arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias");
            ArquivoTarefas arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas", stopWords);
            ArquivoRotulos arqRotulos = new ArquivoRotulos(Rotulo.class.getConstructor(), "rotulos");

            ListaInvertida lista = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");

            Categorias_ctl ctlCategorias = new Categorias_ctl(arqCategorias, arqTarefas);
            Tarefas_ctl ctlTarefas = new Tarefas_ctl(arqTarefas, arqCategorias, arqRotulos);
            Rotulos_ctl ctlRotulos = new Rotulos_ctl(arqRotulos, arqTarefas);

            Scanner scanner = new Scanner(System.in, "UTF-8");

            MenuCategorias menuCategorias = new MenuCategorias(ctlCategorias, ctlTarefas);
            MenuTarefas menuTarefas = new MenuTarefas(ctlTarefas, ctlCategorias, ctlRotulos, lista, stopWords);
            MenuRotulos menuRotulos = new MenuRotulos(ctlRotulos, ctlTarefas);
            MenuStopWords menuStopWords = new MenuStopWords(stopWords);
            MenuBackups menuBackups = new MenuBackups(scanner, ".\\dados", ".\\backup");

            int opcao;
            do {
                System.out.println("--------------");
                System.out.println("|--- MENU ---|");
                System.out.println("--------------");
                System.out.println(">Inicio ");
                System.out.println("1- Categorias");
                System.out.println("2- Tarefas");
                System.out.println("3- Rotulos");
                System.out.println("4- Backup");
                System.out.println("0- Encerrar");
                System.out.print("Escolha uma opcao: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        menuCategorias.menu();
                        break;
                    case 2:
                        menuTarefas.menu();
                        break;
                    case 3:
                        menuRotulos.menu();
                        break;
                    case 4:
                        menuBackups.menu();
                        break;
                    case 5:
                        menuStopWords.menu();
                        break;
                    case 0:
                        System.out.println("\nSaindo...");
                        break;
                    default:
                        System.out.println("\nOpcao invalida. Tente novamente.");
                        break;
                }
            } while (opcao != 0);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
