import java.util.ArrayList;

public class Rotulos_ctl {
    private ArquivoRotulos arqRotulos;
    private ArquivoTarefas arqTarefas;

    public Rotulos_ctl(ArquivoRotulos arqRotulo, ArquivoTarefas arqTarefas) {
        this.arqRotulos = arqRotulo;
        this.arqTarefas = arqTarefas;
    }

    public boolean adicionarRotulo(Rotulo rotulo) throws Exception {
        arqRotulos.create(rotulo);
        return true;
    }

    public boolean atualizarRotulo(Rotulo rotulo) throws Exception {
        return arqRotulos.update(rotulo);
    }

    public boolean excluirRotulo(int idTarefa) throws Exception {
        return arqRotulos.delete(idTarefa);
    }

    public ArrayList<Rotulo> getRotulosByTarefa(int idTarefa) throws Exception {
        return arqRotulos.getRotulosByTarefa(idTarefa);
    }

    public ArrayList<Rotulo> getRotulos() throws Exception {
        return arqRotulos.getRotulos();
    }

    public ArrayList<Tarefa> getTarefas() throws Exception {
        return arqTarefas.getTarefas();
    }
}
