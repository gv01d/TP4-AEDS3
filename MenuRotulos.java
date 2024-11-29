import java.util.ArrayList;
import java.util.Scanner;

public class MenuRotulos {
    private Rotulos_ctl ctlRotulos;
    private Scanner scanner;

    public MenuRotulos(Rotulos_ctl ctlRotulos, Tarefas_ctl ctlTarefas) {
        this.ctlRotulos = ctlRotulos;

        this.scanner = new Scanner(System.in);
    }

    public void menu() throws Exception {
        int opcao;
        do {
            System.out.println("-----------------");
            System.out.println("> Inicio > Rotulos\n");
            System.out.println("1- Incluir");
            System.out.println("2- Excluir");
            System.out.println("3- Atualizar");
            System.out.println("4- Listar");
            System.out.println("0- Voltar");
            System.out.print("\nEscolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarRotulo();
                    break;
                case 2:
                    excluirTarefa();
                    break;
                case 3:
                    atualizarRotulo();
                    break;
                case 4:
                    listarRotulctlRotulos();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);
    }

    private void adicionarRotulo() throws Exception {

        // ------------------------------------------------------------------------------------
        // Nome
        System.out.print("Nome do Rotulo: ");
        String nome = scanner.nextLine();

        // ------------------------------------------------------------------------------------
        // Adicionando a rotulo
        Rotulo rotulo = new Rotulo(nome);

        if (ctlRotulos.adicionarRotulo(rotulo)) {
            System.out.println("Rotulo adicionada com sucesso!");
        } else {
            System.out.println("Erro ao adicionar tarefa.");
        }
    }

    private void atualizarRotulo() throws Exception {
        ArrayList<Rotulo> rotulos = ctlRotulos.getRotulos();

        // ------------------------------------------------------------------------------------
        // Escolha de Rotulo
        if (rotulos.isEmpty()) {
            System.out.println("Nenhum rotulo encontrada para atualizar.");
            return;
        }

        System.out.println("Escolha o rotulo a ser atualizado:");
        for (int i = 0; i < rotulos.size(); i++) {
            System.out.println((i + 1) + ") " + rotulos.get(i).getNome());
        }
        System.out.println("0) Fim");

        System.out.print("Opcao: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        while (escolha < 0 || escolha > rotulos.size()) {
            System.out.println("Opcao invalida.");
            escolha = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Opcao: ");
        }
        if (escolha == 0) {
            return;
        }

        Rotulo rotulo = rotulos.get(escolha - 1);

        // ------------------------------------------------------------------------------------
        // Atualizando Nome
        System.out.print("Nome da Rotulo: ");
        String nome = scanner.nextLine();
        rotulo.setNome(nome);

        // ------------------------------------------------------------------------------------
        // Atualizar Tarefa
        if (ctlRotulos.atualizarRotulo(rotulo)) {
            System.out.println("Tarefa atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar a rotulo.");
        }
    }

    private void excluirTarefa() throws Exception {
        // ------------------------------------------------------------------------------------
        ArrayList<Rotulo> rotulos = ctlRotulos.getRotulos();

        if (rotulos.isEmpty()) {
            System.out.println("Nenhuma rotulo encontrada para excluir.");
            return;
        }

        System.out.println("Escolha a rotulo a ser excluida:");
        for (int i = 0; i < rotulos.size(); i++) {
            System.out.println((i + 1) + ") " + rotulos.get(i).getNome());
        }

        // Escolha de opçoes
        System.out.print("Opcao: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        while (escolha < 0 || escolha > rotulos.size()) {
            System.out.println("Opcao invalida.");
            escolha = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Opcao: ");
        }
        if (escolha == 0) {
            return;
        }

        int idRotulo = rotulos.get(escolha - 1).getId();

        // ------------------------------------------------------------------------------------
        // excluindo a rotulo
        if (ctlRotulos.excluirRotulo(idRotulo)) {
            System.out.println("Rotulo excluida com sucesso!");
        } else {
            System.out.println("Erro ao excluir rotulo.");
        }
    }

    private void listarRotulctlRotulos() throws Exception {
        ArrayList<Rotulo> rotulos = ctlRotulos.getRotulos();
        if (rotulos.isEmpty()) {
            System.out.println("Nenhuma rotulo para listar");
        } else {
            System.out.println("Lista de Rotulos:");
            for (Rotulo rotulo : rotulos) {
                System.out.println(rotulo.toString());
            }
        }
    }

}
