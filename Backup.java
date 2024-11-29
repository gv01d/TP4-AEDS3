import java.io.RandomAccessFile;

public class Backup {

    int id;
    byte[] data;
    String[] files;

    RandomAccessFile arquivo;
    // Cabeçalho: int para id, long para tamanho de cada arquivo, short tamanho do
    // nome ,string para nome de cada arquivo
    final int TAM_CABECALHO = 8;
    final int TAM_CABECALHO_ARQUIVO = 8;

    void BackupFiles() {
        // Backup files
    }

    void loadFile(String file) throws Exception {
        arquivo = new RandomAccessFile(file, "rw");
        byte[] bt = new byte[(int) arquivo.length() + TAM_CABECALHO_ARQUIVO];

        arquivo.seek(0);
    }

    void LoadBackup() {
        // Restore files
    }

}