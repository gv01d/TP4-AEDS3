import java.lang.reflect.Constructor;
import java.util.ArrayList;

import aed3.Arquivo;
import aed3.ArvoreBMais;

public class ArquivoRotulos extends Arquivo<Rotulo> {
    private ArvoreBMais<ParIdId> indiceTarefaRotulo;
    private ArvoreBMais<ParIdId> indiceRotuloTarefa;

    public ArquivoRotulos(Constructor<Rotulo> construtor, String nomeArquivo) throws Exception {
        super(construtor, nomeArquivo);
        indiceTarefaRotulo = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/arvoreTarefaPorRotulo.db");
        indiceRotuloTarefa = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/arvoreRotuloPorTarefa.db");
    }

    @Override
    public void create(Rotulo rotulo) throws Exception {
        super.create(rotulo);
        indiceTarefaRotulo.create(new ParIdId(rotulo.getId(), -1));
        indiceRotuloTarefa.create(new ParIdId(-2, rotulo.getId()));

    }

    public void createN(int idRotulo, ArrayList<Integer> id2) throws Exception {
        for (int id : id2) {
            indiceTarefaRotulo.create(new ParIdId(idRotulo, id));
            indiceRotuloTarefa.create(new ParIdId(id, idRotulo));
        }
    }

    @Override
    public Rotulo read(int id) throws Exception {
        return super.read(id);
    }

    public ArrayList<Integer> getN(int idRotulo) throws Exception {
        ArrayList<Integer> tarefas = new ArrayList<Integer>();

        for (ParIdId idP : indiceTarefaRotulo.read(new ParIdId(idRotulo, -1))) {
            tarefas.add(idP.getId2());
        }

        return tarefas;
    }

    @Override
    public boolean update(Rotulo rotulo) throws Exception {
        Rotulo antigaRotulo = read(rotulo.getId());
        if (antigaRotulo != null) {
            return super.update(rotulo);
        }
        return false;
    }

    public void updateN(int idRotulo, ArrayList<Integer> id2) throws Exception {
        ArrayList<ParIdId> antigoRelacionamento = indiceTarefaRotulo.read(new ParIdId(idRotulo, -1));

        for (ParIdId oldId : antigoRelacionamento) {
            if (!id2.contains(oldId.getId2())) {
                indiceTarefaRotulo.delete(new ParIdId(idRotulo, oldId.getId2()));
                indiceRotuloTarefa.delete(new ParIdId(oldId.getId2(), idRotulo));
            } else {
                id2.remove(Integer.valueOf(oldId.getId2()));
            }
        }
        for (int id : id2) {
            indiceTarefaRotulo.create(new ParIdId(idRotulo, id));
            indiceRotuloTarefa.create(new ParIdId(id, idRotulo));
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        Rotulo rotulo = read(id);
        if (rotulo != null) {
            for (ParIdId idR : indiceTarefaRotulo.read(new ParIdId(id, -1))) {
                indiceTarefaRotulo.delete(new ParIdId(id, idR.getId2()));
                indiceRotuloTarefa.delete(new ParIdId(idR.getId2(), id));
            }
            indiceTarefaRotulo.delete(new ParIdId(id, -1));
            return super.delete(id);
        }
        return false;
    }

    public ArrayList<Rotulo> getRotulos() throws Exception {

        ArrayList<Rotulo> rotulos = readAll();

        return rotulos;
    }

    public ArrayList<Rotulo> getRotulosByTarefa(int idTarefa) throws Exception {
        ArrayList<Rotulo> rotulos = new ArrayList<>();

        ArrayList<ParIdId> pares = indiceRotuloTarefa.read(new ParIdId(idTarefa, -1));
        for (ParIdId idR : pares) {
            Rotulo rotulo = read(idR.getId2());
            if (rotulo != null) {
                rotulos.add(rotulo);
            }
        }

        return rotulos;
    }
}
