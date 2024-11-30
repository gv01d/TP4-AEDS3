import java.util.HashMap;
import java.util.Scanner;

public class MenuBackups {

    private Scanner scanner;
    Backup backup;

    String defaultFolder = ".\\dados";

    public MenuBackups(Scanner scanner) throws Exception {
        this.scanner = scanner;
        backup = new Backup(defaultFolder);
    }

    public MenuBackups(Scanner scanner, String folderToBackupFrom, String backupFolder) throws Exception {
        this.scanner = scanner;
        this.defaultFolder = folderToBackupFrom;
        backup = new Backup(defaultFolder, backupFolder);
    }

    public void menu() throws Exception {
        int opcao;
        do {

            System.out.println("-----------------");
            System.out.println("> Inicio > Backup\n");
            System.out.println("1- Criar Backup");
            System.out.println("2- Excluir Backup");
            System.out.println("3- Carregar Backup");
            System.out.println("0- Voltar");
            System.out.print("\nEscolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    criarBackup();
                    break;
                case 2:
                    excluirBackup();
                    break;
                case 3:
                    carregarBackup();
                    break;
                case 4:
                    listarBackups();
                    break;
                case 5:
                    refatorar();
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

    public void criarBackup() throws Exception {
        backup.setFilesFromFolder(defaultFolder);
        backup.backupFiles();
        System.out.println("<> Backup criado com sucesso.");
    }

    public void excluirBackup() throws Exception {

        HashMap<Integer, String> list = backup.listBackups();
        System.out.println("Escolha o backup que deseja excluir: ");
        int i = 0;
        int[] keys = new int[list.size()];
        for (int k : list.keySet()) {
            keys[i] = k;
            i++;
        }
        i = 1;
        for (int k : keys) {
            System.out.println("\t" + i + ") " + list.get(k));
            i++;
        }
        System.out.print("\t0) Cancelar\n");

        // select backup to delete
        System.out.print("opção: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("<> Operacao cancelada.");
            return;
        }
        backup.deleteBackup(keys[escolha - 1]);
        ;
        System.out.println("<> Backup excluido com sucesso.");
    }

    public void carregarBackup() throws Exception {
        HashMap<Integer, String> list = backup.listBackups();
        System.out.println("Escolha o backup que deseja carregar: ");
        int i = 0;
        int[] keys = new int[list.size()];
        for (int k : list.keySet()) {
            keys[i] = k;
            i++;
        }
        i = 1;
        for (int k : keys) {
            System.out.println("\t" + i + ") " + list.get(k));
            i++;
        }
        System.out.print("\t0) Cancelar\n");

        // select backup to load
        System.out.print("opção: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("<> Operacao cancelada.");
            return;
        }
        backup.restoreBackup(keys[escolha - 1]);
        System.out.println("<> Backup carregado com sucesso.");
    }

    public void listarBackups() throws Exception {
        HashMap<Integer, String> list = backup.listBackups();
        System.out.println("Backups disponiveis: ");
        int i = 1;
        for (String k : list.values()) {
            System.out.println("\t" + i + ") " + k);
        }
        System.out.println();
    }

    public void refatorar() throws Exception {
        backup.refactor();
        System.out.println("<> Dados refatorados com sucesso.");
    }

}
