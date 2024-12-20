import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import aed3.LZW;

public class Backup {
    int id;

    LocalDate dataCriacao;
    ArrayList<String> files;
    ArrayList<byte[]> data;

    LZW lzw;
    String backupFolder = ".\\backup";

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
     * > int id, int amount : (byte lapide, int id, int data, String file)
     */

    public Backup() throws Exception {
        this.dataCriacao = LocalDate.now();
        this.files = new ArrayList<>();
        this.data = new ArrayList<>();
        this.lzw = new LZW();

        // Load gerenciador
        File f = new File(backupFolder);
        if (!f.exists())
            f.mkdir();
        RandomAccessFile gerenciador = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");
        if (gerenciador.length() == 0) {
            gerenciador.writeInt(-1);
            gerenciador.writeInt(0);
            gerenciador.writeInt(0);
        }
        gerenciador.seek(0);
        this.id = gerenciador.readInt();
        gerenciador.close();
    }

    public Backup(String folder, boolean reset) throws Exception {
        this();
        if (reset) {
            this.files = new ArrayList<>();
        }
        File f = new File(folder);
        if (!f.exists())
            f.mkdir();
        else {
            String[] files = f.list();
            if (files != null) {
                for (String file : files) {
                    this.files.add(folder + "\\" + file);
                }
            }
        }
    }

    public void setFilesFromFolder(String folder) {
        File f = new File(folder);
        if (!f.exists())
            f.mkdir();
        else {
            String[] files = f.list();
            if (files != null) {
                this.files.clear();
                for (String file : files) {
                    this.files.add(folder + "\\" + file);
                }
            }
        }
    }

    public float findPercentage(int id) throws Exception {
        float total = 0;
        setFilesFromFolder(".\\dados");
        for (String file : files) {
            total += new File(file).length();
        }
        RandomAccessFile arquivo = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");
        arquivo.seek(4);
        int amount = arquivo.readInt();
        int maxAmount = arquivo.readInt();
        int idBackup;
        byte lapide;
        File f = null;
        for (int i = 0; i < maxAmount; i++) {
            lapide = arquivo.readByte();
            idBackup = arquivo.readInt();
            if (idBackup == id && lapide == ' ') {
                arquivo.skipBytes(4);
                f = new File(arquivo.readUTF());
                break;
            } else {
                arquivo.skipBytes(4);
                arquivo.readUTF();
            }
            
        }
        arquivo.close();
        if (f == null) {
            return -1;
        }
        System.out.println("Total: " + total);
        System.out.println("Backup: " + f.length());
        return (f.length() / total) * 100;
    }

    public Backup(String folder, String backupFolder) throws Exception {
        this(folder);
        this.backupFolder = backupFolder;
    }

    public Backup(String folder) throws Exception {
        this(folder, true);
    }

    public Backup(ArrayList<String> files) throws Exception {
        this();
        this.files = files;
    }

    public Backup(String[] files) throws Exception {
        this();
        for (String file : files) {
            this.files.add(file);
        }
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public void addFile(String file) {
        files.add(file);
    }

    public HashMap<Integer, String> listBackups() throws Exception {
        HashMap<Integer, String> str = new HashMap<>();
        RandomAccessFile arquivo = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");
        arquivo.seek(4);
        int amount = arquivo.readInt();
        int maxAmount = arquivo.readInt();
        int id;
        byte lapide;
        for (int i = 0; i < maxAmount; i++) {
            lapide = arquivo.readByte();
            id = arquivo.readInt();
            if (lapide == ' ') {
                str.put(id, "Backup#" + id + "-" + LocalDate.ofEpochDay(arquivo.readInt()) + ": "
                        + arquivo.readUTF());
            } else {
                arquivo.skipBytes(4);
                arquivo.readUTF();
            }
        }
        arquivo.close();
        return str;
    }

    public void deleteBackup(int id) throws Exception {
        RandomAccessFile arquivo = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");
        arquivo.seek(4);
        int amount = arquivo.readInt();
        int maxAmount = arquivo.readInt();
        int idBackup;
        byte lapide;
        for (int i = 0; i < maxAmount; i++) {
            lapide = arquivo.readByte();
            idBackup = arquivo.readInt();
            if (idBackup == id && lapide == ' ') {
                arquivo.seek(arquivo.getFilePointer() - 5);
                arquivo.writeByte('*');
                arquivo.seek(4);
                arquivo.writeInt(--amount);
                break;
            } else {
                arquivo.skipBytes(4);
                arquivo.readUTF();
            }
            
        }
        arquivo.close();
    }

    public void refactor() throws Exception {
        RandomAccessFile arquivo = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");
        RandomAccessFile arquivoTemp = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackupTemp.db", "rw");
    
        File f;
        int id = arquivo.readInt();
        int amount = arquivo.readInt();
        System.out.println(amount);
    
        if (amount > 0) {
            arquivoTemp.writeInt(id);
        } else {
            this.id = -1;
            arquivoTemp.writeInt(-1);
        }
    
        int maxAmount = arquivo.readInt();
        arquivoTemp.writeInt(amount);
        arquivoTemp.writeInt(maxAmount); // Corrigir duplicação
    
        for (int i = 0; i < maxAmount; i++) {
            byte lapide = arquivo.readByte();
            int idBackup = arquivo.readInt();
    
            if (lapide == ' ') {
                System.out.println("não é lápide");
                arquivoTemp.writeByte(' ');
                arquivoTemp.writeInt(idBackup);
                arquivoTemp.writeInt(arquivo.readInt());
                arquivoTemp.writeUTF(arquivo.readUTF());
            } else {
                System.out.println("lápide");
                arquivo.skipBytes(4);
                String name = arquivo.readUTF();
                f = new File(name);
                f.delete();
            }
        }
    
        // Copiar os dados do arquivo temporário de volta para o arquivo original
        arquivo.seek(0);
        arquivoTemp.seek(0);
        byte[] data = new byte[(int) arquivoTemp.length()];
        arquivoTemp.readFully(data);
        arquivo.setLength(data.length); // Ajusta o tamanho
        arquivo.write(data);
    
        arquivo.seek(8);
        arquivo.writeInt(amount);
        // Fechar os arquivos
        arquivo.close();
        arquivoTemp.close();
    
        // Excluir arquivo temporário
        File tempFile = new File(backupFolder + "\\gerenciadorDeBackupTemp.db");
        tempFile.delete();
    }
    
    // ---------------------------------------------------------------------------------------------------------------------------

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

    // ---------------------------------------------------------------------------------------------------------------------------
    // //
    // Backup files to a file and update the manager

    public void backupFiles() throws Exception {
        id++;
        // -------------------------------------
        // Load files to *byte array*
        data.clear();
        for (String file : files) {
            byte[] fileData = loadFile(file);
            data.add(fileData);
        }

        // -------------------------------------
        // Open backup file
        dataCriacao = LocalDate.now();
        String nomeDoArquivo = backupFolder + "\\" + id + "." + dataCriacao.getDayOfMonth() + "-"
                + dataCriacao.getMonthValue()
                + "-" + dataCriacao.getYear() + ".db";
        RandomAccessFile arquivo = new RandomAccessFile(nomeDoArquivo, "rw");

        // -------------------------------------
        // Create header for backup file
        // < id, data, amount >
        arquivo.writeInt(id);
        arquivo.writeInt((int) dataCriacao.toEpochDay());
        arquivo.writeInt(files.size());

        // -------------------------------------
        // encode data
        // < file names, file data >
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < data.size(); i++) {
            dos.writeUTF(files.get(i));
            dos.writeInt(data.get(i).length);
            dos.write(data.get(i));
        }

        byte[] encoded = lzw.codifica(baos.toByteArray());
        arquivo.write(encoded);

        arquivo.close();

        // -------------------------------------
        // Update gerenciador
        arquivo = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");

        // header < id, amount >

        arquivo.seek(0);
        arquivo.writeInt(id);
        int amount = arquivo.readInt();
        int maxAmount = arquivo.readInt();
        arquivo.seek(4);
        arquivo.writeInt(++amount);
        arquivo.writeInt(++maxAmount);

        // backup < id, data, file name >
        arquivo.seek(arquivo.length());
        arquivo.writeByte(' ');
        arquivo.writeInt(id);
        arquivo.writeInt((int) dataCriacao.toEpochDay());
        arquivo.writeUTF(nomeDoArquivo);

        arquivo.close();

    }

    byte[] loadFile(String file) throws Exception {
        // -------------------------------------
        // open the file
        RandomAccessFile arquivo = new RandomAccessFile(file, "rw");
        byte[] fileData = new byte[(int) arquivo.length()];
        arquivo.read(fileData);

        // Retorna o backup em byte[]
        arquivo.close();
        return fileData;
    }

    // ---------------------------------------------------------------------------------------------------------------------------
    // //
    // Restore backup from a file
    public void restoreBackup(int id) throws Exception {
        // -------------------------------------
        // Open manager file
        RandomAccessFile arquivo = new RandomAccessFile(backupFolder + "\\gerenciadorDeBackup.db", "rw");

        // < id, amount >
        arquivo.seek(8);
        int maxAmount = arquivo.readInt();

        // -------------------------------------
        // search for the backup
        // < id, data, file name >
        byte lapide;
        int idBackup;
        String temp;
        for (int i = 0; i < maxAmount; i++) {
            lapide = arquivo.readByte();
            idBackup = arquivo.readInt();
            arquivo.skipBytes(4);
            temp = arquivo.readUTF();
            if (idBackup == id && lapide == ' ') {
                restoreBackup(temp);
                break;
            }
        }

        arquivo.close();
    }

    public void restoreBackup(String nome) throws Exception {
        // -------------------------------------
        // Open backup file
        RandomAccessFile arquivo = new RandomAccessFile(nome, "rw");

        // -------------------------------------
        // header
        // < id, data, amount >
        id = arquivo.readInt();
        dataCriacao = LocalDate.ofEpochDay(arquivo.readInt());
        int amount = arquivo.readInt();

        byte[] temp;

        // -------------------------------------
        // decode
        // < file names, file data >
        byte[] decoded = new byte[(int) arquivo.length() - 12];
        arquivo.read(decoded);
        decoded = lzw.decodifica(decoded);
        ByteArrayInputStream bais = new ByteArrayInputStream(decoded);
        DataInputStream dis = new DataInputStream(bais);

        //
        files.clear();
        data.clear();
        for (int i = 0; i < amount; i++) {
            files.add(dis.readUTF());
            int sizeFile = dis.readInt();
            temp = new byte[sizeFile];
            dis.read(temp);
            data.add(temp);
        }

        arquivo.close();

        // -------------------------------------
        // Restore files
        for (int i = 0; i < files.size(); i++) {
            restoreFile(files.get(i), data.get(i));
        }
    }

    // Load and restore backups
    void restoreFile(String file, byte[] data) throws Exception {
        // -------------------------------------
        // abre o arquivo
        RandomAccessFile arquivo = new RandomAccessFile(file, "rw");
        arquivo.setLength(0);
        arquivo.write(data);
        arquivo.close();
    }

}
