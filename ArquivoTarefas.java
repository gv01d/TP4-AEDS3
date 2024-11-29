import aed3.Arquivo;
import aed3.ArvoreBMais;
import aed3.ElementoLista;
import aed3.ListaInvertida;

import java.util.ArrayList;
import java.util.HashMap;

import java.lang.reflect.Constructor;
import java.text.Normalizer;

public class ArquivoTarefas extends Arquivo<Tarefa> {

    private ArvoreBMais<ParIdId> indiceCategoriaTarefa;
    private ArvoreBMais<ParIdId> indiceRotuloTarefa;
    private ArvoreBMais<ParIdId> indiceTarefaRotulo;

    private StopWords stopWords;

    private ListaInvertida list;

    // Relacionamento entre tarefas e categorias
    public ArquivoTarefas(Constructor<Tarefa> construtor, String nomeArquivo, StopWords stopWords) throws Exception {
        super(construtor, nomeArquivo);
        indiceRotuloTarefa = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/arvoreRotuloPorTarefa.db");
        indiceTarefaRotulo = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/arvoreTarefaPorRotulo.db");
        indiceCategoriaTarefa = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5,
                "dados/arvoreTarefasPorCategoria.db");
        list = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");

        this.stopWords = stopWords;
    }

    Exception a = null;

    @Override
    public void create(Tarefa tarefa) throws Exception {
        super.create(tarefa);
        indiceCategoriaTarefa.create(new ParIdId(tarefa.getIdCategoria(), tarefa.getId()));

        list.incrementaEntidades();

        // Adicionando no indice invertido
        String[] temp = read(tarefa.getId()).nome.split(" ");
        for (String word : temp) {
            word = Normalizer.normalize(word, Normalizer.Form.NFD).toLowerCase();
        }
        String[] words = stopWords.filter(temp);

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (String word : words) {
            if (!map.containsKey(word)) {
                map.put(word, 0);
            }
            map.put(word, map.get(word) + 1);
        }
        map.forEach((k, v) -> {
            try {
                list.create(k, new ElementoLista(tarefa.getId(), v / words.length));
            } catch (Exception e) {
                a = e;
            }
        });

        if (a != null) {
            Exception e = a;
            a = null;
            throw (e);
        }
    }

    public void createN(int idTarefa, ArrayList<Integer> id2) throws Exception {
        for (int id : id2) {
            indiceRotuloTarefa.create(new ParIdId(idTarefa, id));
            indiceTarefaRotulo.create(new ParIdId(id, idTarefa));
        }
    }

    @Override
    public Tarefa read(int id) throws Exception {
        return super.read(id);
    }

    public ArrayList<Integer> getN(int idTarefa) throws Exception {
        ArrayList<Integer> rotulos = new ArrayList<Integer>();

        for (ParIdId idP : indiceRotuloTarefa.read(new ParIdId(idTarefa, -1))) {
            rotulos.add(idP.getId2());
        }

        return rotulos;
    }

    @Override
    public boolean update(Tarefa tarefa) throws Exception {
        Tarefa antigaTarefa = read(tarefa.getId());

        if (antigaTarefa == null) {
            return false;
        }

        // deletando antigos no indice invertido
        String[] oldWords = stopWords.filter(antigaTarefa.nome.split(" "));

        for (String word : oldWords) {
            list.delete(word, tarefa.getId());
        }

        // Adicionando no indice invertido
        String[] words = stopWords.filter(tarefa.nome.split(" "));

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (String word : words) {
            if (!map.containsKey(word)) {
                map.put(word, 0);
            }
            map.put(word, map.get(word) + 1);
        }
        map.forEach((k, v) -> {
            try {
                list.create(k, new ElementoLista(tarefa.getId(), v / words.length));
            } catch (Exception e) {
                a = e;
            }
        });

        if (a != null) {
            Exception e = a;
            a = null;
            throw (e);
        }

        // update nos indices
        if (antigaTarefa.getIdCategoria() != tarefa.getIdCategoria()) {
            indiceCategoriaTarefa.delete(new ParIdId(antigaTarefa.getIdCategoria(), antigaTarefa.getId()));
            indiceCategoriaTarefa.create(new ParIdId(tarefa.getIdCategoria(), tarefa.getId()));
        }

        return super.update(tarefa);
    }

    public void updateN(int idTarefa, ArrayList<Integer> id2) throws Exception {
        ArrayList<ParIdId> antigaTarefa = indiceRotuloTarefa.read(new ParIdId(idTarefa, -1));

        for (ParIdId oldId : antigaTarefa) {
            if (!id2.contains(oldId.getId2())) {
                indiceRotuloTarefa.delete(new ParIdId(idTarefa, oldId.getId2()));
                indiceTarefaRotulo.delete(new ParIdId(oldId.getId2(), idTarefa));
            } else {
                id2.remove(Integer.valueOf(oldId.getId2()));
            }
        }
        for (int id : id2) {
            indiceRotuloTarefa.create(new ParIdId(idTarefa, id));
            indiceTarefaRotulo.create(new ParIdId(id, idTarefa));
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        Tarefa tarefa = read(id);

        // deletando no indice invertido
        String[] words = stopWords.filter(read(id).nome.split(" "));

        for (String word : words) {
            list.delete(word, id);
        }

        // deletando nos indices
        if (tarefa != null) {
            indiceCategoriaTarefa.delete(new ParIdId(tarefa.getIdCategoria(), id));
            return super.delete(id);
        }
        list.decrementaEntidades();

        return false;
    }

    public void deleteN(int idTarefa) throws Exception {
        ArrayList<ParIdId> antigaTarefa = indiceRotuloTarefa.read(new ParIdId(idTarefa, -1));

        for (ParIdId pid : antigaTarefa) {
            indiceRotuloTarefa.delete(new ParIdId(idTarefa, pid.getId2()));
            indiceTarefaRotulo.delete(new ParIdId(pid.getId2(), idTarefa));
        }
    }

    public ArrayList<Tarefa> buscarPorCategoria(int idCategoria) throws Exception {
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        ArrayList<ParIdId> pares = indiceCategoriaTarefa.read(new ParIdId(idCategoria, -1));
        for (ParIdId par : pares) {
            Tarefa tarefa = read(par.getId2());
            if (tarefa != null) {
                tarefas.add(tarefa);
            }
        }
        return tarefas;
    }

    public ArrayList<Tarefa> getTarefas() throws Exception {
        ArrayList<Tarefa> tarefas = new ArrayList<>();

        ArrayList<ParIdId> pares = indiceCategoriaTarefa.read(null);

        for (ParIdId par : pares) {
            Tarefa tarefa = read(par.getId2());
            if (tarefa != null) {
                tarefas.add(tarefa);
            }
        }

        return tarefas;
    }

    public ArrayList<Tarefa> getTarefasbyRotulos(ArrayList<Integer> idRotulo) throws Exception {
        ArrayList<Tarefa> tarefas = new ArrayList<>();

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int id : idRotulo) {
            ArrayList<ParIdId> pares = indiceRotuloTarefa.read(new ParIdId(id, -1));
            for (ParIdId par : pares) {
                Tarefa tarefa = read(par.getId2());
                if (tarefa != null) {
                    map.put(tarefa.getId(), map.getOrDefault(tarefa.getId(), 0) + 1);
                }
            }
        }

        for (int id : map.keySet()) {
            if (map.get(id) == idRotulo.size()) {
                tarefas.add(read(id));
            }
        }

        return tarefas;
    }
}
