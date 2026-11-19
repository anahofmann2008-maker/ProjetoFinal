
package control;

import BD.BDBolos;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Bolos;

public class CBolos {

    JTextField jTextFieldnome;
    JTextField jTextFieldsabor;
    JTextArea jTextAreareceita;
    JTextField jTextFieldID;
    JTextField jTextFieldPesquisa;

    JList<String> jListbolos;
    JList<String> jListFavoritos;

    ArrayList<Bolos> listaBolos = new ArrayList<>();
    ArrayList<Bolos> listaExibida = new ArrayList<>();
    ArrayList<Integer> favoritos = new ArrayList<>();

    DefaultListModel<String> defaultListModel = new DefaultListModel<>();
    DefaultListModel<String> modeloFavoritos = new DefaultListModel<>();

    JComboBox<String> jComboBoxnota;
    JButton jButtonSalvarEditar;
    JButton jButtonFavorito;

    int index = -1;

    BDBolos bd = new BDBolos();

    public CBolos(JTextField nome, JTextField sabor,
            JTextArea receita, JList<String> lista,
            JButton botao, JTextField id,
            JComboBox<String> nota, JTextField pesquisa,
            JList<String> listaFavoritos, JButton botaoFavorito) {

        this.jTextFieldnome = nome;
        this.jTextFieldsabor = sabor;
        this.jTextAreareceita = receita;
        this.jListbolos = lista;
        this.jButtonSalvarEditar = botao;
        this.jTextFieldID = id;
        this.jComboBoxnota = nota;
        this.jTextFieldPesquisa = pesquisa;
        this.jListFavoritos = listaFavoritos;
        this.jButtonFavorito = botaoFavorito;

        listaBolos = bd.carregarBanco();
        favoritos = bd.carregarFavoritos();

        jListbolos.setModel(defaultListModel);
        jListFavoritos.setModel(modeloFavoritos);

        jButtonFavorito.addActionListener(e -> alternarFavorito());

        jListbolos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarTextoBotaoFavorito();
            }
        });

        jListFavoritos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarFavorito();
            }
        });

        jTextFieldPesquisa.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                carregarLista();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                carregarLista();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                carregarLista();
            }
        });

        limpar();
        carregarLista();
        carregarListaFavoritos();
    }

    public void limpar() {
        jTextFieldnome.setText("");
        jTextAreareceita.setText("");
        jTextFieldsabor.setText("");

        jButtonSalvarEditar.setText("Salvar");
        jTextFieldID.setText(gerarId() + "");

        if (jComboBoxnota.getItemCount() > 0) {
            jComboBoxnota.setSelectedIndex(0);
        }

        jTextFieldPesquisa.setText("");
        jListbolos.clearSelection();
        jListFavoritos.clearSelection();

        index = -1;
        atualizarTextoBotaoFavorito();
    }

    public int gerarId() {
        int novoId = 1;

        for (Bolos b : listaBolos) {
            if (b.getId() >= novoId) {
                novoId = b.getId() + 1;
            }
        }

        return novoId;
    }

    public void CadastrarBolos() {
        int id = Integer.parseInt(jTextFieldID.getText());
        String nome = jTextFieldnome.getText().trim();
        String sabor = jTextFieldsabor.getText().trim();
        String receita = jTextAreareceita.getText().trim();

        if (nome.isEmpty() || sabor.isEmpty() || receita.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            return;
        }

        if (jComboBoxnota.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecione uma nota!");
            return;
        }

        String nota = jComboBoxnota.getSelectedItem().toString();

        Bolos b = new Bolos(id, nome, sabor, receita, nota);

        if (index == -1) {
            listaBolos.add(b);
            JOptionPane.showMessageDialog(null, "Bolo cadastrado com sucesso!");
        } else {
            listaBolos.set(index, b);
            JOptionPane.showMessageDialog(null, "Bolo editado com sucesso!");
        }

        bd.salvarBanco(listaBolos);

        limpar();
        carregarLista();
        carregarListaFavoritos();
    }

    public void carregarLista() {
        defaultListModel.clear();
        listaExibida.clear();

        String pesquisa = jTextFieldPesquisa.getText().toLowerCase().trim();

        for (Bolos b : listaBolos) {
            if (b.getNome().toLowerCase().contains(pesquisa)
                    || b.getSabor().toLowerCase().contains(pesquisa)) {

                listaExibida.add(b);

                defaultListModel.addElement(
                        "ID: " + b.getId() + " - " + b.getNome()
                );
            }
        }

        jListbolos.setModel(defaultListModel);
    }

    public void selecionarObjeto() {
        int indiceSelecionado = jListbolos.getSelectedIndex();

        if (indiceSelecionado >= 0
                && indiceSelecionado < listaExibida.size()) {

            Bolos b = listaExibida.get(indiceSelecionado);
            preencherCampos(b);
        }
    }

    public void alternarFavorito() {
        int selecionado = jListbolos.getSelectedIndex();

        if (selecionado < 0 || selecionado >= listaExibida.size()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Selecione uma receita primeiro!"
            );
            return;
        }

        Bolos b = listaExibida.get(selecionado);

        if (favoritos.contains(b.getId())) {
            favoritos.remove(Integer.valueOf(b.getId()));

            JOptionPane.showMessageDialog(
                    null,
                    "Receita removida dos favoritos!"
            );
        } else {
            favoritos.add(b.getId());

            JOptionPane.showMessageDialog(
                    null,
                    "Receita adicionada aos favoritos!"
            );
        }

        bd.salvarFavoritos(favoritos);

        carregarListaFavoritos();
        atualizarTextoBotaoFavorito();
    }

    public void carregarListaFavoritos() {
        modeloFavoritos.clear();

        for (Bolos b : listaBolos) {
            if (favoritos.contains(b.getId())) {
                modeloFavoritos.addElement(
                        "ID: " + b.getId() + " - " + b.getNome()
                );
            }
        }

        jListFavoritos.setModel(modeloFavoritos);
    }

    public void selecionarFavorito() {
        int selecionado = jListFavoritos.getSelectedIndex();

        if (selecionado < 0 || selecionado >= modeloFavoritos.size()) {
            return;
        }

        String item = modeloFavoritos.getElementAt(selecionado);

        for (Bolos b : listaBolos) {
            String identificacao = "ID: " + b.getId() + " - " + b.getNome();

            if (identificacao.equals(item)) {
                preencherCampos(b);
                return;
            }
        }
    }

    private void preencherCampos(Bolos b) {
        index = listaBolos.indexOf(b);

        jTextFieldID.setText(String.valueOf(b.getId()));
        jTextFieldnome.setText(b.getNome());
        jTextFieldsabor.setText(b.getSabor());
        jTextAreareceita.setText(b.getReceita());
        jTextAreareceita.setCaretPosition(0);

        jComboBoxnota.setSelectedItem(b.getNota());

        jButtonSalvarEditar.setText("Editar");
        atualizarTextoBotaoFavorito();
    }

    public void atualizarTextoBotaoFavorito() {
        int selecionado = jListbolos.getSelectedIndex();

        if (selecionado >= 0 && selecionado < listaExibida.size()) {
            Bolos b = listaExibida.get(selecionado);

            if (favoritos.contains(b.getId())) {
                jButtonFavorito.setText("★ Remover dos favoritos");
            } else {
                jButtonFavorito.setText("☆ Favoritar");
            }
        } else {
            jButtonFavorito.setText("☆ Favoritar");
        }
    }
}
