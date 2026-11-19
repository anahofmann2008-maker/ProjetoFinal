
package control;

import BD.BDPudim;
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
import model.Pudim;

public class CPudim {

    private JTextField jTextFieldnome;
    private JTextField jTextFieldsabor;
    private JTextField jTextFieldID;
    private JTextField jTextFieldPesquisa;
    private JTextArea jTextAreareceita;

    private JList<String> jListPudim;
    private JList<String> jListFavoritos;

    private JButton jButtonSalvarEditar;
    private JButton jButtonFavorito;

    private JComboBox<String> jComboBoxnota;

    private ArrayList<Pudim> listaPudim = new ArrayList<>();
    private ArrayList<Pudim> listaExibida = new ArrayList<>();
    private ArrayList<Pudim> listaFavoritosExibida = new ArrayList<>();
    private ArrayList<Integer> favoritos = new ArrayList<>();

    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private DefaultListModel<String> modeloFavoritos = new DefaultListModel<>();

    private int index = -1;

    public CPudim(
            JTextField nome,
            JTextField sabor,
            JTextArea receita,
            JList<String> lista,
            JButton botao,
            JTextField id,
            JComboBox<String> nota,
            JTextField pesquisa,
            JList<String> listaFavoritos,
            JButton botaoFavorito) {

        this.jTextFieldnome = nome;
        this.jTextFieldsabor = sabor;
        this.jTextAreareceita = receita;
        this.jListPudim = lista;
        this.jButtonSalvarEditar = botao;
        this.jTextFieldID = id;
        this.jComboBoxnota = nota;
        this.jTextFieldPesquisa = pesquisa;
        this.jListFavoritos = listaFavoritos;
        this.jButtonFavorito = botaoFavorito;

        listaPudim = BDPudim.carregarBanco();
        favoritos = BDPudim.carregarFavoritos();

        jListPudim.setModel(modeloLista);
        jListFavoritos.setModel(modeloFavoritos);

        jButtonFavorito.addActionListener(e -> alternarFavorito());

        jListPudim.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (jListPudim.getSelectedIndex() >= 0) {
                    jListFavoritos.clearSelection();
                }
                atualizarTextoBotaoFavorito();
            }
        });

        jListFavoritos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (jListFavoritos.getSelectedIndex() >= 0) {
                    jListPudim.clearSelection();
                    selecionarFavorito();
                }
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
        jTextFieldsabor.setText("");
        jTextAreareceita.setText("");
        jTextFieldID.setText(String.valueOf(gerarId()));

        if (jComboBoxnota.getItemCount() > 0) {
            jComboBoxnota.setSelectedIndex(0);
        }

        jButtonSalvarEditar.setText("Salvar");
        jTextFieldPesquisa.setText("");

        jListPudim.clearSelection();
        jListFavoritos.clearSelection();

        index = -1;

        atualizarTextoBotaoFavorito();
    }

    public int gerarId() {
        int maiorId = 0;

        for (Pudim p : listaPudim) {
            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }

        return maiorId + 1;
    }

    public void CadastrarPudim() {

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

        int id = Integer.parseInt(jTextFieldID.getText());
        String nota = jComboBoxnota.getSelectedItem().toString();

        Pudim p = new Pudim(id, nome, sabor, receita, nota);

        if (index == -1) {
            listaPudim.add(p);
            JOptionPane.showMessageDialog(null, "Pudim cadastrado com sucesso!");
        } else {
            listaPudim.set(index, p);
            JOptionPane.showMessageDialog(null, "Pudim editado com sucesso!");
        }

        BDPudim.salvarBanco(listaPudim);

        limpar();
        carregarLista();
        carregarListaFavoritos();
    }

    public void carregarLista() {

        modeloLista.clear();
        listaExibida.clear();

        String pesquisa = jTextFieldPesquisa.getText()
                .trim().toLowerCase();

        for (Pudim p : listaPudim) {

            if (p.getNome().toLowerCase().contains(pesquisa)
                    || p.getSabor().toLowerCase().contains(pesquisa)) {

                listaExibida.add(p);

                modeloLista.addElement(
                        "ID: " + p.getId() + " - " + p.getNome()
                );
            }
        }

        jListPudim.setModel(modeloLista);
    }

    public void selecionarObjeto() {

        int selecionado = jListPudim.getSelectedIndex();

        if (selecionado < 0 || selecionado >= listaExibida.size()) {
            return;
        }

        Pudim p = listaExibida.get(selecionado);

        preencherCampos(p);
    }

    public void selecionarFavorito() {

        int selecionado = jListFavoritos.getSelectedIndex();

        if (selecionado < 0
                || selecionado >= listaFavoritosExibida.size()) {
            return;
        }

        Pudim p = listaFavoritosExibida.get(selecionado);

        preencherCampos(p);
    }

    private void preencherCampos(Pudim p) {

        index = listaPudim.indexOf(p);

        jTextFieldID.setText(String.valueOf(p.getId()));
        jTextFieldnome.setText(p.getNome());
        jTextFieldsabor.setText(p.getSabor());
        jTextAreareceita.setText(p.getReceita());
        jTextAreareceita.setCaretPosition(0);

        jComboBoxnota.setSelectedItem(p.getNota());

        jButtonSalvarEditar.setText("Editar");

        atualizarTextoBotaoFavorito();
    }

    private Pudim obterPudimSelecionado() {

        int selecionadoFavoritos = jListFavoritos.getSelectedIndex();

        if (selecionadoFavoritos >= 0
                && selecionadoFavoritos < listaFavoritosExibida.size()) {
            return listaFavoritosExibida.get(selecionadoFavoritos);
        }

        int selecionadoLista = jListPudim.getSelectedIndex();

        if (selecionadoLista >= 0
                && selecionadoLista < listaExibida.size()) {
            return listaExibida.get(selecionadoLista);
        }

        return null;
    }

    public void alternarFavorito() {

        Pudim p = obterPudimSelecionado();

        if (p == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Selecione um pudim primeiro!"
            );
            return;
        }

        int id = p.getId();

        if (favoritos.contains(id)) {
            favoritos.remove(Integer.valueOf(id));

            JOptionPane.showMessageDialog(
                    null,
                    "Receita removida dos favoritos!"
            );
        } else {
            favoritos.add(id);

            JOptionPane.showMessageDialog(
                    null,
                    "Receita adicionada aos favoritos!"
            );
        }

        BDPudim.salvarFavoritos(favoritos);

        carregarListaFavoritos();
        atualizarTextoBotaoFavorito();
    }

    public void carregarListaFavoritos() {

        modeloFavoritos.clear();
        listaFavoritosExibida.clear();

        for (Pudim p : listaPudim) {

            if (favoritos.contains(p.getId())) {

                listaFavoritosExibida.add(p);

                modeloFavoritos.addElement(
                        "ID: " + p.getId() + " - " + p.getNome()
                );
            }
        }

        jListFavoritos.setModel(modeloFavoritos);
    }

    public void atualizarTextoBotaoFavorito() {

        Pudim p = obterPudimSelecionado();

        if (p != null && favoritos.contains(p.getId())) {
            jButtonFavorito.setText("★ Remover dos favoritos");
        } else {
            jButtonFavorito.setText("☆ Favoritar");
        }
    }
}
