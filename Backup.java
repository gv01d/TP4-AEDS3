import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;

public class Backup {
    int id;
    int amountOfBackupFiles;
    LocalDate dataCriacao;
    ArrayList<String> files;
    ArrayList<byte[]> data;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /*
     * 
     * Estrutura do arquivo de backup:
     * > Nome do arquivo: ( Backup <id> <dataCriação>.db )
     * > int id, data ,int amount : (String file, int size, byte[] data)
     * 
     */

    /*
     * Estrutura do gerenciador:
     * > int id, int amount : (int id, data, String file)
     */

    public Backup() throws Exception {
        this.id = -1;
        this.amountOfBackupFiles = 0;
        this.dataCriacao = LocalDate.now();
        this.files = new ArrayList<>();
        this.data = new ArrayList<>();

        // Load gerenciador
        RandomAccessFile gerenciador = new RandomAccessFile(".\\backup\\gerenciadorDeBackup.db", "rw");
        gerenciador.seek(0);
        this.id = gerenciador.readInt();
        this.amountOfBackupFiles = gerenciador.readInt();
        gerenciador.close();
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public void addFile(String file) {
        files.add(file);
    }

    public ArrayList<String> listBackups() throws Exception {
        ArrayList<String> str = new ArrayList<>();
        RandomAccessFile arquivo = new RandomAccessFile(".\\backup\\gerenciadorDeBackup.db", "rw");
        arquivo.seek(4);
        int amount = arquivo.readInt();
        for (int i = 0; i < amount; i++) {
            str.add("Backup " + arquivo.readInt() + " - " + LocalDate.ofEpochDay(arquivo.readInt()) + " : "
                    + arquivo.readUTF());
        }
        arquivo.close();
        return str;
    }

    //

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // id, data, amount
        dos.writeInt(id);
        dos.writeInt((int) this.dataCriacao.toEpochDay());
        dos.writeInt(files.size());

        // files, data
        for (int i = 0; i < files.size(); i++) {
            dos.writeUTF(files.get(i));
            dos.writeInt(data.get(i).length);
            dos.write(data.get(i));
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        // id, data, amount
        id = dis.readInt();
        dataCriacao = LocalDate.ofEpochDay(dis.readInt());
        int amount = dis.readInt();

        // files, data
        for (int i = 0; i < amount; i++) {
            files.add(dis.readUTF());
            int sizeFile = dis.readInt();
            data.add(dis.readNBytes(sizeFile));
        }
    }

    // Backup files

    public void backupFiles() throws Exception {
        // Backup files
        for (String file : files) {
            byte[] fileData = loadFile(file);
            data.add(fileData);
        }

        dataCriacao = LocalDate.now();
        String nomeDoArquivo = ".\\Backup\\" + id + "." + dataCriacao.getDayOfMonth() + "/"
                + dataCriacao.getMonthValue()
                + "/" + dataCriacao.getYear() + ".db";
        RandomAccessFile arquivo = new RandomAccessFile(nomeDoArquivo, "rw");

        // id, data, amount
        arquivo.writeInt(++id);
        arquivo.writeInt((int) dataCriacao.toEpochDay());
        arquivo.writeInt(files.size());

        for (int i = 0; i < data.size(); i++) {
            arquivo.writeUTF(files.get(i));
            arquivo.writeInt(data.get(i).length);
            arquivo.write(data.get(i));
        }

        arquivo.close();
        // Update gerenciador
        arquivo = new RandomAccessFile("\\backup\\gerenciadorDeBackup.db", "rw");

        arquivo.seek(0);
        arquivo.writeInt(id);
        arquivo.writeInt(++amountOfBackupFiles);

        arquivo.seek(arquivo.length());
        arquivo.writeInt(id);
        arquivo.writeInt((int) dataCriacao.toEpochDay());
        arquivo.writeUTF(nomeDoArquivo);

        arquivo.close();

    }

    byte[] loadFile(String file) throws Exception {
        // abre o arquivo
        RandomAccessFile arquivo = new RandomAccessFile(file, "rw");
        byte[] fileData = new byte[(int) arquivo.length()];
        arquivo.read(fileData);

        // Retorna o backup em byte[]
        arquivo.close();
        return fileData;
    }

    public void restoreBackup(String nome) throws Exception {
        RandomAccessFile arquivo = new RandomAccessFile(nome, "rw");

        // id, data, amount
        id = arquivo.readInt();
        dataCriacao = LocalDate.ofEpochDay(arquivo.readInt());
        int amount = arquivo.readInt();

        byte[] temp;
        // files, data
        for (int i = 0; i < amount; i++) {
            files.add(arquivo.readUTF());
            int sizeFile = arquivo.readInt();
            temp = new byte[sizeFile];
            arquivo.read(temp);
            data.add(temp);
        }

        arquivo.close();

        //
        for (int i = 0; i < files.size(); i++) {
            restoreFile(files.get(i), data.get(i));
        }
    }

    // Load and restore backups

    void restoreFile(String file, byte[] data) throws Exception {
        // abre o arquivo
        RandomAccessFile arquivo = new RandomAccessFile(file, "rw");
        arquivo.setLength(0);
        arquivo.write(data);
        arquivo.close();
    }

}
