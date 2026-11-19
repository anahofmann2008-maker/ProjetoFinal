package BD;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import model.Bolos;

public class BDBolos {

    public static void salvarBanco(ArrayList<Bolos> dados) {
        try {
            FileOutputStream fs = new FileOutputStream("Bolo.jass");
            ObjectOutputStream oos = new ObjectOutputStream(fs);

            oos.writeObject(dados);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Bolos> carregarBanco() {
        ArrayList<Bolos> clientes = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream("Bolo.jass");
            ObjectInputStream ois = new ObjectInputStream(fis);

            clientes = (ArrayList<Bolos>) ois.readObject();
            ois.close();

        } catch (Exception e) {
        }

        return clientes;
    }

    public static void salvarFavoritos(ArrayList<Integer> favoritos) {
        try {
            FileOutputStream fs = new FileOutputStream("FavoritosBolos.jass");
            ObjectOutputStream oos = new ObjectOutputStream(fs);

            oos.writeObject(favoritos);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> carregarFavoritos() {
        ArrayList<Integer> favoritos = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream("FavoritosBolos.jass");
            ObjectInputStream ois = new ObjectInputStream(fis);

            favoritos = (ArrayList<Integer>) ois.readObject();
            ois.close();

        } catch (Exception e) {
        }

        return favoritos;
    }
}
