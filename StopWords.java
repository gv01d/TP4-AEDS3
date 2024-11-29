import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;

public class StopWords {

    RandomAccessFile arquivo;
    String nomeArquivo;
    final int TAM_CABECALHO = 8;

    // ----------------------------- Construtor -----------------------------//
    public StopWords(String n) throws Exception {
        // Cria o diretório, se necessário
        File f = new File(".\\dados");
        if (!f.exists())
            f.mkdir();
        this.nomeArquivo = n;
        arquivo = new RandomAccessFile(".\\dados\\" + n + ".db", "rw");

        // coloca numero de valores no cabeçalho
        if (arquivo.length() < TAM_CABECALHO) {
            arquivo.seek(0);
            arquivo.writeInt(0);
            arquivo.writeInt(0);
        }

    }

    // ----------------------------- Create -----------------------------//
    public void create(String word) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(Normalizer.normalize(word, Normalizer.Form.NFD).toLowerCase());

        byte[] bt = baos.toByteArray();

        // Grava o novo registro
        arquivo.seek(arquivo.length());
        arquivo.writeByte(' ');
        arquivo.writeShort((short) bt.length);
        arquivo.write(bt);

        // Grava no cabeçalho a quantia
        arquivo.seek(0);
        int n = arquivo.readInt();
        int m = arquivo.readInt();
        arquivo.seek(0);
        arquivo.writeInt(n);
        arquivo.writeInt(m);
    }

    public void create(String[] words) throws Exception {
        // Grava no cabeçalho a quantia
        arquivo.seek(0);
        int n = arquivo.readInt();
        int m = arquivo.readInt();
        arquivo.seek(0);
        arquivo.writeInt(n + words.length);
        arquivo.writeInt(m + words.length);

        for (int i = 0; i < words.length; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(
                    Normalizer.normalize(words[i], Normalizer.Form.NFD).toLowerCase());

            byte[] bt = baos.toByteArray();

            // Gava o novo registro
            arquivo.seek(arquivo.length());
            arquivo.writeByte(' ');
            arquivo.writeShort((short) bt.length);
            arquivo.write(bt);
        }
    }

    // ----------------------------- Read ------------------------------- //
    public String[] read() throws Exception {
        arquivo.seek(0);
        int n = arquivo.readInt();
        arquivo.skipBytes(4);

        short tam;
        byte[] b;
        byte lapide;

        String[] ret = new String[n];

        for (int i = 0; i < n; i++) {
            lapide = arquivo.readByte();
            tam = arquivo.readShort();
            if (lapide == ' ') {
                b = new byte[tam];
                arquivo.read(b);

                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                DataInputStream dis = new DataInputStream(bais);
                ret[i] = dis.readUTF();
            } else {
                i--;
                arquivo.skipBytes(tam);
            }
        }
        return ret;
    }

    // ---------------------------- Delete ------------------------------ //

    public void delete(String word) throws Exception {
        arquivo.seek(0);
        int n = arquivo.readInt();
        arquivo.skipBytes(4);

        int a = n;

        short tam;
        byte[] b;
        byte lapide;

        String temp;

        for (int i = 0; i < n; i++) {
            long pos = arquivo.getFilePointer();
            lapide = arquivo.readByte();
            tam = arquivo.readShort();
            if (lapide == ' ') {
                b = new byte[tam];
                arquivo.read(b);

                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                DataInputStream dis = new DataInputStream(bais);
                temp = dis.readUTF();
                if (temp.equals(Normalizer.normalize(word, Normalizer.Form.NFD)
                        .toLowerCase())) {
                    arquivo.seek(pos);
                    arquivo.writeByte('x');
                    a--;
                }
            } else {
                n--;
                arquivo.skipBytes(tam);
            }
        }
        arquivo.seek(0);
        arquivo.writeInt(a);
    }

    // --------------------------- Get amount ----------------------------- //
    public int getTotalAmount() throws Exception {
        arquivo.seek(4);
        return arquivo.readInt();
    }

    // ------------------------ Get Live amount -------------------------- //

    public int getAmount() throws Exception {
        arquivo.seek(0);
        return arquivo.readInt();
    }

    // -------------------------- Refactor Data ---------------------------- //

    public void refactorData() throws Exception {
        String[] liveData = read();

        arquivo.setLength(0);
        arquivo.seek(0);
        arquivo.writeInt(0);
        arquivo.writeInt(0);

        create(liveData);
    }

    // ------------------------------ Filter -------------------------------- //
    public String[] filter(String[] words) throws Exception {
        String[] stopWords = read();
        ArrayList<String> lt = new ArrayList<String>();
        HashMap<String, Integer> map = new HashMap<>();

        for (String w : words) {
            w = Normalizer.normalize(w, Normalizer.Form.NFD).toLowerCase();
            map.put(w, 1);
        }

        for (String w : stopWords) {
            map.remove(w);
        }

        String[] ret = new String[map.size()];

        map.forEach((k, v) -> {
            lt.add(k);
        });

        for (int i = 0; i < lt.size(); i++) {
            ret[i] = lt.get(i);
        }

        return ret;
    }

}
