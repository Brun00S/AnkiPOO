package view;

import controller.AnkiController;
import model.Baralho;
import model.Flashcard;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import static view.CorTema.*;

public class TelaCadastroBaralho extends JPanel {
    private JComboBox<String> tipoBox;
    private JComboBox<String> baralhoBox;
    private JTextField frenteField;
    private JTextField versoField;

    private final AnkiController controller;
    private final Runnable aoVoltar;

    public TelaCadastroBaralho(AnkiController controller, Runnable aoVoltar) {
        this.controller = controller;
        this.aoVoltar = aoVoltar;

        setLayout(new BorderLayout());
        setBackground(FUNDO.get());

        // Criação e configuração do Título
        JLabel titulo = new JLabel("Adicionar Cartão", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 32));
        titulo.setForeground(FONTE.get());
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        //Criação e configuração do Formulário
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.setBackground(FUNDO.get());
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel tipoLabel = new JLabel("Tipo: ");
        tipoLabel.setFont(new Font("Serif", Font.BOLD, 24));
        tipoLabel.setForeground(FONTE.get());
        tipoBox = new JComboBox<>(new String[]{"Texto"});
        tipoBox.setBackground(FUNDO.get());
        tipoBox.setForeground(FONTE.get());

        formulario.add(tipoLabel);
        formulario.add(tipoBox);

        // Criação e configuração do dropdown de baralhos
        JLabel baralhoLabel = new JLabel("Baralho:");
        baralhoLabel.setFont(new Font("Serif", Font.BOLD, 24));
        baralhoLabel.setForeground(FONTE.get());

        baralhoBox = new JComboBox<>();
        baralhoBox.setBackground(FUNDO.get());
        baralhoBox.setForeground(FONTE.get());

        formulario.add(baralhoLabel);
        formulario.add(baralhoBox);

        // Criação e configuração do campo "frente"
        JLabel frenteLabel = new JLabel("Frente:");
        frenteLabel.setFont(new Font("Serif", Font.BOLD, 24));
        frenteLabel.setForeground(FONTE.get());
        frenteField = new JTextField();
        frenteField.setBackground(FUNDO.get());
        frenteField.setForeground(FONTE.get());

        formulario.add(frenteLabel);
        formulario.add(frenteField);

        // Criação e configuração do campo "verso"
        JLabel versoLabel = new JLabel("Verso:");
        versoLabel.setFont(new Font("Serif", Font.BOLD, 24));
        versoLabel.setForeground(FONTE.get());
        versoField = new JTextField();
        versoField.setBackground(FUNDO.get());
        versoField.setForeground(FONTE.get());

        formulario.add(versoLabel);
        formulario.add(versoField);

        add(formulario, BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        botoes.setBackground(FUNDO.get());

        JButton adicionarBaralhoBtn = new JButton("Adicionar Baralho");
        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");

        configurarBotao(adicionarBaralhoBtn, new Color(0, 78, 152));
        configurarBotao(salvarBtn, new Color(60, 110, 113));
        configurarBotao(cancelarBtn, new Color(164, 22, 26));

        botoes.add(adicionarBaralhoBtn);
        botoes.add(salvarBtn);
        botoes.add(cancelarBtn);
        add(botoes, BorderLayout.SOUTH);

        // Ações

        adicionarBaralhoBtn.addActionListener(e -> mostrarDialogoNovoBaralho());
        cancelarBtn.addActionListener(e -> aoVoltar.run());
        salvarBtn.addActionListener(e -> salvarCartao());

        atualizarDropdown(); // inicializa e atualiza o dropdown ao abrir
    }

    private void configurarBotao(JButton botao, Color cor) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setForeground(FONTE.get());
        botao.setBackground(cor);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    //Cria e mostra a tela de adição de novo baralho

    private void mostrarDialogoNovoBaralho() {
        JTextField nomeField = new JTextField();
        Object[] mensagem = {"Nome do novo baralho:", nomeField};

        int opcao = JOptionPane.showOptionDialog(this, mensagem, "Criar Novo Baralho",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new String[]{"Criar", "Cancelar"}, "Criar");

        if (opcao == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            if (!nome.isEmpty()) {
                controller.adicionarBaralho(nome);
                atualizarDropdown();
                baralhoBox.setSelectedItem(nome);
            } else {
                JOptionPane.showMessageDialog(this, "Nome inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Cria e salva o cartão(flashcard) no baralho
    private void salvarCartao() {
        String baralhoNome = (String) baralhoBox.getSelectedItem();
        String frente = frenteField.getText().trim();
        String verso = versoField.getText().trim();

        if (baralhoNome == null || frente.isEmpty() || verso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Flashcard novo = new Flashcard(frente, verso, LocalDate.now(), baralhoNome);
        controller.adicionarFlashcard(baralhoNome, novo);

        frenteField.setText("");
        versoField.setText("");
        JOptionPane.showMessageDialog(this, "Cartão adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void atualizarDropdown() {
        baralhoBox.removeAllItems();
        for (Baralho b : controller.getConjunto().getTodos()) {
            baralhoBox.addItem(b.getNomeBaralho());
        }
    }
}
