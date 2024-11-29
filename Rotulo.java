import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aed3.Registro;

public class Rotulo implements Registro {
    private int id;
    private String name;

    public Rotulo() {
        this(-1, "");
    }

    public Rotulo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rotulo(String name) {
        this.id = -1;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.name;
    }

    public void setNome(String name) {
        this.name = name;
    }

    public String toString() {

        return "   ID: " + id + " | Nome: " + name;

    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(name);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        name = dis.readUTF();
    }

    public int compareTo(Object o) {
        return this.id - ((Rotulo) o).id;
    }
}
