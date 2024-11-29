import java.util.ArrayList;

public class Tarefas_ctl {
    private ArquivoTarefas arqTarefas;
    private ArquivoCategorias arqCategorias;
    private ArquivoRotulos arqRotulos;

    public Tarefas_ctl(ArquivoTarefas arqTarefas, ArquivoCategorias arqCategorias, ArquivoRotulos arqRotulos) {
        this.arqTarefas = arqTarefas;
        this.arqCategorias = arqCategorias;
        this.arqRotulos = arqRotulos;
    }

    public boolean adicionarTarefa(Tarefa tarefa, ArrayList<Integer> rotulos) throws Exception {
        arqTarefas.create(tarefa);
        arqTarefas.createN(tarefa.getId(), rotulos);
        return true;
    }

    public boolean atualizarTarefa(Tarefa tarefa, ArrayList<Integer> rotulos) throws Exception {
        boolean b = arqTarefas.update(tarefa);
        arqTarefas.updateN(tarefa.getId(), rotulos);
        return b;

    }

    public boolean excluirTarefa(int idTarefa) throws Exception {
        boolean b = arqTarefas.delete(idTarefa);
        arqTarefas.deleteN(idTarefa);
        return b;
    }

    public ArrayList<Integer> getRotulosId(int idTarefa) throws Exception {
        return arqTarefas.getN(idTarefa);
    }

    public Tarefa getTarefaByID(int id) throws Exception {
        return arqTarefas.read(id);
    }

    public ArrayList<Tarefa> getTarefas() throws Exception {
        return arqTarefas.getTarefas();
    }

    public ArrayList<Tarefa> getTarefasByCategoria(int idCategoria) throws Exception {
        return arqTarefas.buscarPorCategoria(idCategoria);
    }

    public ArrayList<Tarefa> getTarefasByRotulos(ArrayList<Integer> idRotulo) throws Exception {
        return arqTarefas.getTarefasbyRotulos(idRotulo);
    }

    public ArrayList<Categoria> getCategorias() throws Exception {
        return arqCategorias.getCategorias();
    }

    public ArrayList<Rotulo> getRotulos() throws Exception {
        return arqRotulos.getRotulos();
    }
}
