
public class ArquivoBackup {

    int id;
    int amountOfBackupFiles;
    byte[] data;
    String[] files;

    String gerenciadorDeBackup;

    RandomAccessFile gerenciador;
    RandomAccessFile arquivo;
    // Cabeçalho: int para id, long para tamanho de cada arquivo, short tamanho do
    // nome ,string para nome de cada arquivo
    final int TAM_CABECALHO = 8;
    final int TAM_CABECALHO_ARQUIVO = 6;

    Backup(String gerenciadorDeBackup) throws Exception {
        this.gerenciador = new RandomAccessFile(gerenciadorDeBackup, "rw");
        this.gerenciadorDeBackup = gerenciadorDeBackup;

        byte[] gerenciadorData = new byte[(int) gerenciador.length()];
        gerenciador.read(gerenciadorData);
        ByteArrayInputStream bais = new ByteArrayInputStream(gerenciadorData);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.amountOfBackupFiles = dis.readInt();
    }

    void BackupFiles() throws Exception {
        // get id
        byte[] gerenciadorData = new byte[(int) gerenciador.length()];
        gerenciador.read(gerenciadorData);

        // Backup files
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Cabeçalho
        dos.writeInt(id);

        // Grava os arquivos no DOS
        for (String file : files) {
            dos.write(loadFile(file));
        }
    }

    byte[] loadFile(String file) throws Exception {
        // abre o arquivo
        arquivo = new RandomAccessFile(file, "rw");

        // coloca numero de valores no cabeçalho
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(arquivo.length());
        dos.writeShort(file.length());
        dos.writeUTF(file);

        // Grava o arquivo no DOS
        byte[] fileData = new byte[(int) arquivo.length()];
        arquivo.read(fileData);
        dos.write(fileData);

        // Grava o arquivo no byte[] com o cabeçalho
        byte[] bt = new byte[(int) arquivo.length() + TAM_CABECALHO_ARQUIVO + file.length()];
        bt = baos.toByteArray();

        // Retorna o backup em byte[]
        return bt;
    }

    void LoadBackup() {
        // Restore files
    }

}