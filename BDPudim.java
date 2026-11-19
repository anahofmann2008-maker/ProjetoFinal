
package BD;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import model.Pudim;

public class BDPudim {

    public static void salvarBanco(ArrayList<Pudim> dados) {
        try {
            FileOutputStream arquivo = new FileOutputStream("Pudim.jass");
            ObjectOutputStream objeto = new ObjectOutputStream(arquivo);

            objeto.writeObject(dados);

            objeto.close();
            arquivo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Pudim> carregarBanco() {
        try {
            FileInputStream arquivo = new FileInputStream("Pudim.jass");
            ObjectInputStream objeto = new ObjectInputStream(arquivo);

            ArrayList<Pudim> dados = (ArrayList<Pudim>) objeto.readObject();

            objeto.close();
            arquivo.close();

            return dados;

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void salvarFavoritos(ArrayList<Integer> favoritos) {
        try {
            FileOutputStream arquivo = new FileOutputStream("FavoritosPudim.jass");
            ObjectOutputStream objeto = new ObjectOutputStream(arquivo);

            objeto.writeObject(favoritos);

            objeto.close();
            arquivo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Integer> carregarFavoritos() {
        try {
            FileInputStream arquivo = new FileInputStream("FavoritosPudim.jass");
            ObjectInputStream objeto = new ObjectInputStream(arquivo);

            ArrayList<Integer> dados = (ArrayList<Integer>) objeto.readObject();

            objeto.close();
            arquivo.close();

            return dados;

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
