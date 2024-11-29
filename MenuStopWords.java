import java.util.Scanner;

public class MenuStopWords {

    private Scanner scanner;

    private StopWords stopWords;

    public MenuStopWords(StopWords stopWords) {
        this.stopWords = stopWords;
        this.scanner = new Scanner(System.in);
    }

    public void menu() throws Exception {
        int opcao;
        do {
            System.out.println("-----------------");
            System.out.println("> Inicio > Stop Words\n");
            System.out.println("1- Incluir");
            System.out.println("2- Excluir");
            System.out.println("3- Atualizar");
            System.out.println("4- Listar");
            System.out.println("5- Refactor");
            System.out.println("0- Voltar");
            System.out.print("\nEscolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarStopWords();
                    break;
                case 2:
                    excluirStopWords();
                    break;
                case 3:
                    atualizarStopWords();
                    break;
                case 4:
                    listarStopWords();
                    break;
                case 5:
                    RefactorStopWords();
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

    void adicionarStopWords() throws Exception {
        System.out.println(
                "Palavras ( EOF to stop : CTRL-D para LINUX and MacOS, CTRL-Z seguido de <return> para WIndows) ");
        System.out.print(": ");

        String[] words = scanner.nextLine().split(" ");
        stopWords.create(words);
    }

    void excluirStopWords() throws Exception {
        System.out.println(
                "Palavras ( EOF to stop : CTRL-D para LINUX and MacOS, CTRL-Z seguido de <return> para WIndows) ");
        System.out.print(": ");
        String[] words = scanner.nextLine().split(" ");
        for (String word : words) {
            stopWords.delete(word);
        }
    }

    void atualizarStopWords() throws Exception {
        System.out.println(
                "Palavra para atualizar ( Antiga , Nova )");
        System.out.print(": ");
        String[] words = scanner.nextLine().split(" ");
        if (words.length == 1) {
            stopWords.delete(words[0]);
            stopWords.create(words[1]);
        } else {
            if (words.length == 1) {
                System.out.print(": ");
                String[] words2 = scanner.nextLine().split(" ");
                if (words2.length == 1) {
                    stopWords.delete(words[0]);
                    stopWords.create(words2[0]);
                } else {
                    System.out.println("Numero Invalido de Parametros");
                }
            } else {
                System.out.println("Numero Invalido de Parametros");
            }
        }
    }

    void listarStopWords() throws Exception {
        String[] words = stopWords.read();
        for (String word : words) {
            System.out.println(word);
        }
    }

    void RefactorStopWords() throws Exception {
        stopWords.refactorData();
    }
}