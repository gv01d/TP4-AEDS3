import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import aed3.ElementoLista;
import aed3.ListaInvertida;

public class MenuTarefas {
    private Tarefas_ctl ctlTarefas;
    private Categorias_ctl ctlCategorias;
    private Rotulos_ctl ctlRotulos;
    private Scanner scanner;

    private StopWords stopWords;

    private ListaInvertida list;

    public MenuTarefas(Tarefas_ctl ctlTarefas, Categorias_ctl ctlCategorias, Rotulos_ctl ctlRotulos,
            ListaInvertida list, StopWords stopWords) {
        this.ctlTarefas = ctlTarefas;
        this.ctlCategorias = ctlCategorias;
        this.ctlRotulos = ctlRotulos;
        this.list = list;
        this.stopWords = stopWords;
        this.scanner = new Scanner(System.in);
    }

    public void menu() throws Exception {
        int opcao;
        do {
            System.out.println("-----------------");
            System.out.println("> Inicio > Tarefas\n");
            System.out.println("1- Incluir");
            System.out.println("2- Excluir");
            System.out.println("3- Atualizar");
            System.out.println("4- Listar");
            System.out.println("5- Buscar");
            System.out.println("0- Voltar");
            System.out.print("\nEscolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarTarefa();
                    break;
                case 2:
                    excluirTarefa();
                    break;
                case 3:
                    atualizarTarefa();
                    break;
                case 4:
                    listarTarefas();
                    break;
                case 5:
                    buscarTarefas();
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

    // ---------------------------------- < > ---------------------------------- //

    private void adicionarTarefa() throws Exception {

        // ------------------------------------------------------------------------------------
        // Nome
        System.out.print("Nome da Tarefa: ");
        String nome = scanner.nextLine();

        // ----------------------

        // Categoria
        System.out.println("Escolha uma categoria:");
        ArrayList<Categoria> categorias = ctlCategorias.getCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("\t(" + (i + 1) + ") " + categorias.get(i).getNome());
        }

        System.out.print("Opcao: ");
        int escolhaCategoria = scanner.nextInt();
        scanner.nextLine();

        int idCategoria = categorias.get(escolhaCategoria - 1).getId();

        // ----------------------

        // Rotulo
        System.out.println("Escolha os rotulos:");
        ArrayList<Rotulo> rotulos = ctlRotulos.getRotulos();
        for (int i = 0; i < rotulos.size(); i++) {
            System.out.println("\t(" + (i + 1) + ") " + rotulos.get(i).getNome());
        }
        System.out.println("( 0 ) Fim");

        ArrayList<Integer> idRotulos = new ArrayList<Integer>();
        System.out.print("\nOpçao: ");

        int escolhaRotulo = scanner.nextInt();
        while (escolhaRotulo != 0) {
            if (escolhaRotulo > rotulos.size() || escolhaRotulo < 0) {
                System.out.println("Opcao invalida.");
            } else {
                idRotulos.add(rotulos.get(escolhaRotulo - 1).getId());
            }
            escolhaRotulo = scanner.nextInt();
        }
        scanner.nextLine();

        // ----------------------

        // Data de Conclusão
        System.out.println("Escolha uma data de conclusão (dia, mes, ano):");

        int dia = scanner.nextInt();
        int mes = scanner.nextInt();
        int ano = scanner.nextInt();
        scanner.nextLine();
        LocalDate tmp = LocalDate.of(ano, mes, dia);

        System.out.println("Defina o status da tarefa:");
        System.out.println("\t(1) Pendente");
        System.out.println("\t(2) Concluida");
        System.out.print("Opcao: ");
        byte status = scanner.nextByte();

        // Prioridade
        System.out.println("Defina a prioridade da tarefa:");
        System.out.println("\t(1) Baixa");
        System.out.println("\t(2) Media");
        System.out.println("\t(3) Alta");
        System.out.print("Opcao: ");
        byte prioridade = scanner.nextByte();

        // ------------------------------------------------------------------------------------
        // Adicionando a tarefa
        Tarefa tarefa = new Tarefa(nome, LocalDate.now(), tmp, status, prioridade, idCategoria);

        if (ctlTarefas.adicionarTarefa(tarefa, idRotulos)) {
            System.out.println("Tarefa adicionada com sucesso!");
        } else {
            System.out.println("Erro ao adicionar tarefa.");
        }
    }

    // ---------------------------------- < > ---------------------------------- //

    private void atualizarTarefa() throws Exception {
        ArrayList<Tarefa> tarefas = ctlTarefas.getTarefas();

        // ------------------------------------------------------------------------------------
        // Escolha de Tarefa
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada para atualizar.");
            return;
        }

        System.out.println("Escolha a tarefa a ser atualizada:");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + ") " + tarefas.get(i).getNome());
        }
        System.out.println("0 ) Voltar");

        System.out.print("Opcao: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        while (escolha < 0 || escolha > tarefas.size()) {
            System.out.println("(" + escolha + ") Opcao invalida.");
            escolha = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Opcao: ");
        }

        if (escolha == 0) {
            return;
        }

        Tarefa tarefa = tarefas.get(escolha - 1);

        // ------------------------------------------------------------------------------------
        // Atualizando Nome
        System.out.print("Nome da Tarefa: ");
        String nome = scanner.nextLine();
        tarefa.setNome(nome);

        // ----------------------

        // Atualizando Categoria
        System.out.println("Escolha uma categoria:");
        ArrayList<Categoria> categorias = ctlCategorias.getCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("\t(" + (i + 1) + ") " + categorias.get(i).getNome());
        }

        System.out.print("Opcao: ");
        int escolhaCategoria = scanner.nextInt();
        scanner.nextLine();
        //
        int idCategoria = categorias.get(escolhaCategoria - 1).getId();
        tarefa.setIdCategoria(idCategoria);

        // ----------------------

        // Atualizando Rotulo
        System.out.println("Escolha os rotulos:");
        ArrayList<Rotulo> rotulos = ctlRotulos.getRotulos();
        for (int i = 0; i < rotulos.size(); i++) {
            System.out.println("\t(" + (i + 1) + ") " + rotulos.get(i).getNome());
        }
        System.out.println("( 0 ) Fim");

        ArrayList<Integer> idRotulos = new ArrayList<Integer>();
        System.out.print("\nOpçao: ");

        int escolhaRotulo = scanner.nextInt();
        while (escolhaRotulo != 0) {
            if (escolhaRotulo > rotulos.size() || escolhaRotulo < 0) {
                System.out.println("Opcao invalida.");
            } else {
                idRotulos.add(rotulos.get(escolhaRotulo - 1).getId());
            }
            escolhaRotulo = scanner.nextInt();
        }
        scanner.nextLine();

        // ----------------------

        // Atualizando Data de Conclusao
        System.out.println("Escolha uma data de conclusão (dia, mes, ano):");

        int dia = scanner.nextInt();
        int mes = scanner.nextInt();
        int ano = scanner.nextInt();
        scanner.nextLine();
        LocalDate tmp = LocalDate.of(ano, mes, dia);
        tarefa.setDataConclusao(tmp);

        // ----------------------

        // Atualizando Status
        System.out.println("Defina o status da tarefa:");
        System.out.println("\t(1) Pendente");
        System.out.println("\t(2) Concluida");
        System.out.print("Opcao: ");
        byte status = scanner.nextByte();
        tarefa.setStatus(status);

        // ----------------------

        // Atualizando Prioridade
        System.out.println("Defina a prioridade da tarefa:");
        System.out.println("\t(1) Baixa");
        System.out.println("\t(2) Media");
        System.out.println("\t(3) Alta");
        System.out.print("Opcao: ");
        byte prioridade = scanner.nextByte();
        tarefa.setPrioridade(prioridade);

        // ------------------------------------------------------------------------------------
        // Atualizar Tarefa
        if (ctlTarefas.atualizarTarefa(tarefa, idRotulos)) {
            System.out.println("Tarefa atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar a tarefa.");
        }
    }

    // ---------------------------------- < > ---------------------------------- //

    private void excluirTarefa() throws Exception {
        // ------------------------------------------------------------------------------------
        ArrayList<Tarefa> tarefas = ctlTarefas.getTarefas();

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada para excluir.");
            return;
        }

        System.out.println("Escolha a tarefa a ser excluida:");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + ") " + tarefas.get(i).getNome());
        }

        // Escolha de opçoes
        System.out.print("Opcao: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        while (escolha < 0 || escolha > tarefas.size()) {
            System.out.println("Opcao invalida.");
            escolha = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Opcao: ");
        }
        if (escolha == 0) {
            return;
        }

        int idTarefa = tarefas.get(escolha - 1).getId();

        // ------------------------------------------------------------------------------------
        // excluindo a tarefa
        if (ctlTarefas.excluirTarefa(idTarefa)) {
            System.out.println("Tarefa excluida com sucesso!");
        } else {
            System.out.println("Erro ao excluir tarefa.");
        }
    }

    // ---------------------------------- < > ---------------------------------- //

    private void listarTarefas() throws Exception {
        ArrayList<Tarefa> tarefas = ctlTarefas.getTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa para listar");
        } else {
            System.out.println("Lista de Tarefas:");
            for (Tarefa tarefa : tarefas) {
                System.out.println(tarefa.toString());
                System.out.println("Categoria: " + ctlCategorias.getCategoriaById(tarefa.getIdCategoria()).toString());
                System.out.println("Rotulos: ");
                ctlRotulos.getRotulosByTarefa(tarefa.getId()).forEach(
                        Rotulo -> {
                            try {
                                System.out.println(Rotulo.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        }
    }

    // ---------------------------------- < > ---------------------------------- //

    private void buscarTarefas() throws Exception {
        int opcao;
        do {
            System.out.println("-----------------");
            System.out.println("> Inicio > Tarefas > Buscar\n");
            System.out.println("1- Por Nome");
            System.out.println("2- Por Categoria");
            System.out.println("3- Por Rotulos");
            System.out.println("0- Voltar");
            System.out.print("\nEscolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarPorNome();
                    break;
                case 2:
                    buscarPorCategoria();
                    break;
                case 3:
                    buscarRotulos();
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

    // ---------------------------------- < > ---------------------------------- //

    private class SearchElement implements Comparable<SearchElement> {
        private int id;
        private float probability;

        public SearchElement(int id, float probability) {
            this.id = id;
            this.probability = probability;
        }

        @Override
        public int compareTo(SearchElement element) {
            if (this.probability > element.probability) {
                return 1;
            }
            if (this.probability < element.probability) {
                return -1;
            }
            return 0;
        }

    }

    private Float localIDF = null;

    private void buscarPorNome() throws Exception {
        int opt;
        do {

            System.out.println("-----------------");
            System.out.print("\n<>: ");

            // Calculate Probability for Name
            String[] words = scanner.nextLine().split(" ");

            words = stopWords.filter(words);

            HashMap<String, HashMap<Integer, Float>> lmap = new HashMap<>();
            HashMap<Integer, Float> finalMap = new HashMap<>();

            list.numeroEntidades();

            for (String word : words) {
                ElementoLista e[] = list.read(word);

                localIDF = Float.valueOf(e.length) / Float.valueOf(list.numeroEntidades());

                HashMap<Integer, Float> map = new HashMap<>();

                for (ElementoLista a : e) {
                    if (!map.containsKey(a.getId())) {
                        map.put(a.getId(), 0f);
                    }
                    map.put(a.getId(), map.get(a.getId()) + a.getFrequencia());
                }

                map.forEach((k, d) -> {
                    d *= localIDF;
                    if (!finalMap.containsKey(k)) {
                        finalMap.put(k, 0f);
                    }
                    finalMap.put(k, finalMap.get(k) + d);
                });

                lmap.put(word, map);
            }

            ArrayList<SearchElement> searchList = new ArrayList<>();

            finalMap.forEach((k, d) -> {
                searchList.add(new SearchElement(k, d));
            });

            searchList.sort(null);

            // get Tarefas
            if (searchList.isEmpty()) {
                System.out.println("\n<!> Nenhuma tarefa encontrada, -1 para sair, 0 para buscar de novo.");

                System.out.print("Opcao: ");
                // select
                opt = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\nTarefas encontradas : ");
                for (int i = 0; i < searchList.size(); i++) {
                    System.out
                            .println("- " + (i + 1) + " : " + ctlTarefas.getTarefaByID(searchList.get(i).id).getNome());
                }
                System.out.println("- 0 : Buscar de novo");
                System.out.println("- -1 : Sair");

                System.out.print("Opcao: ");
                // select
                opt = scanner.nextInt();
                scanner.nextLine();

                if (opt > 0 && opt <= searchList.size()) {
                    Tarefa tarefa = ctlTarefas.getTarefaByID(searchList.get(opt - 1).id);
                    System.out.println("-----------------");
                    System.out.println(tarefa.toString());
                    System.out.println(
                            "Categoria: " + ctlCategorias.getCategoriaById(tarefa.getIdCategoria()).toString());
                    System.out.println("Rotulos: ");
                    ctlRotulos.getRotulosByTarefa(tarefa.getId()).forEach(
                            Rotulo -> {
                                try {
                                    System.out.println(Rotulo.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                    System.out.println();
                }
            }

        } while (opt == 0);
    }

    private void buscarPorCategoria() throws Exception {

        System.out.println("-----------------");
        System.out.println("Categorias : ");
        ArrayList<Categoria> categorias = ctlCategorias.getCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("\t(" + (i + 1) + ") " + categorias.get(i).getNome());
        }

        System.out.print("Opcao: ");
        int escolhaCategoria = scanner.nextInt();
        scanner.nextLine();

        int idCategoria = categorias.get(escolhaCategoria - 1).getId();

        ArrayList<Tarefa> tarefas = ctlTarefas.getTarefasByCategoria(idCategoria);
        System.out.print("\n Tarefas nessa categoria : \n ");
        for (Tarefa tarefa : tarefas) {
            System.out.println(tarefa.toString());
            System.out.println("Categoria: " + ctlCategorias.getCategoriaById(tarefa.getIdCategoria()).toString());
            System.out.println("Rotulos: ");
            ctlRotulos.getRotulosByTarefa(tarefa.getId()).forEach(
                    Rotulo -> {
                        try {
                            System.out.println(Rotulo.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

        System.out.println("<> Enter para continuar...");
        scanner.nextLine();

        return;
    }

    private void buscarRotulos() throws Exception {

        System.out.println("-----------------");
        System.out.println("Rotulos : ");
        ArrayList<Rotulo> rotulos = ctlRotulos.getRotulos();
        for (int i = 0; i < rotulos.size(); i++) {
            System.out.println("\t(" + (i + 1) + ") " + rotulos.get(i).getNome());
        }
        System.out.println("\n\t( 0 ) Fim");

        ArrayList<Integer> idRotulos = new ArrayList<Integer>();
        System.out.print("\nOpçao: ");

        int escolhaRotulo = scanner.nextInt();
        while (escolhaRotulo != 0) {
            if (escolhaRotulo > rotulos.size() || escolhaRotulo < 0) {
                System.out.println("Opcao invalida.");
            } else {
                idRotulos.add(rotulos.get(escolhaRotulo - 1).getId());
            }
            escolhaRotulo = scanner.nextInt();
        }
        scanner.nextLine();

        ArrayList<Tarefa> tarefas = ctlTarefas.getTarefasByRotulos(idRotulos);
        System.out.print("\n Tarefas com esses rotulos : \n ");
        for (Tarefa tarefa : tarefas) {
            System.out.println(tarefa.toString());
            System.out.println("Categoria: " + ctlCategorias.getCategoriaById(tarefa.getIdCategoria()).toString());
        }

        return;
    }
}
