package view;

import controller.AnkiController;
import model.Baralho;
import model.Flashcard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static view.CorTema.*;

public class TelaCartoesAdicionados extends JPanel {

    private final AnkiController controller;
    private final Runnable aoVoltar;

    private JTable tabela;
    private JComboBox<String> filtroBox;
    private DefaultTableModel modelo;

    private DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaCartoesAdicionados(AnkiController controller, Runnable aoVoltar) {
        this.controller = controller;
        this.aoVoltar = aoVoltar;

        setLayout(new BorderLayout());
        setBackground(FUNDO.get());

        // Título
        JLabel titulo = new JLabel("Cartões Adicionados", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 32));
        titulo.setForeground(FONTE.get());
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Filtro de baralhos (ComboBox)
        JPanel filtroPainel = new JPanel(new FlowLayout());
        filtroPainel.setBackground(FUNDO.get());
        filtroBox = new JComboBox<>();
        filtroBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        filtroBox.setBackground(FUNDO.get());
        filtroBox.setForeground(FONTE.get());
        filtroBox.addActionListener(e -> atualizarTabela());

        filtroPainel.add(new JLabel("Filtrar por baralho:") {{
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setForeground(FONTE.get());
        }});
        filtroPainel.add(filtroBox);
        add(filtroPainel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Frente", "Verso", "Revisão", "Ação"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(30);
        tabela.setBackground(FUNDO.get());
        tabela.setForeground(FONTE.get());
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.setSelectionBackground(new Color(100, 100, 100));
        tabela.setGridColor(new Color(80, 80, 80));

        // Cabeçalho da tabela
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Centralizar colunas de texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabela.getColumnCount() - 1; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(FUNDO.get());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Renderizador de botão
        tabela.getColumn("Ação").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Excluir");
            btn.setBackground(new Color(200, 0, 0));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            return btn;
        });

        // Editor do botão
        tabela.getColumn("Ação").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private final JButton btn = new JButton("Excluir");

            {
                btn.setBackground(new Color(200, 0, 0));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("SansSerif", Font.BOLD, 13));
                btn.setFocusPainted(false);
                btn.addActionListener(e -> {
                    int linha = tabela.getEditingRow();
                    if (linha < 0 || linha >= modelo.getRowCount()) return;

                    String frente = (String) modelo.getValueAt(linha, 0);
                    String verso = (String) modelo.getValueAt(linha, 1);
                    String nomeBaralho = (String) filtroBox.getSelectedItem();

                    Baralho baralho = controller.getConjunto().buscarPorNome(nomeBaralho);
                    if (baralho != null) {
                        Flashcard alvo = baralho.getFlashcards().stream()
                                .filter(f -> f.getFrente().equals(frente) && f.getVerso().equals(verso))
                                .findFirst()
                                .orElse(null);
                        if (alvo != null) {
                            controller.removerFlashcard(nomeBaralho, alvo);
                            atualizarTabela();
                        }
                    }
                });
            }

            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return btn;
            }
        });

        // Botão Voltar
        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        voltarBtn.setBackground(new Color(120, 120, 120));
        voltarBtn.setForeground(Color.WHITE);
        voltarBtn.setFocusPainted(false);
        voltarBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        voltarBtn.addActionListener(e -> aoVoltar.run());

        JPanel rodape = new JPanel();
        rodape.setBackground(FUNDO.get());
        rodape.add(voltarBtn);
        add(rodape, BorderLayout.SOUTH);

        atualizarConteudo(); // inicializa
    }

    public void atualizarConteudo() {
        filtroBox.removeAllItems();
        controller.getConjunto().getTodos().forEach(b -> filtroBox.addItem(b.getNomeBaralho()));
        if (filtroBox.getItemCount() > 0) {
            filtroBox.setSelectedIndex(0);
            atualizarTabela();
        } else {
            modelo.setRowCount(0); // limpa tabela
        }
    }

    private void atualizarTabela() {
        String nomeBaralho = (String) filtroBox.getSelectedItem();
        if (nomeBaralho == null) {
            modelo.setRowCount(0);
            return;
        }

        Baralho baralho = controller.getConjunto().buscarPorNome(nomeBaralho);
        if (baralho == null) {
            modelo.setRowCount(0);
            return;
        }

        List<Flashcard> flashcards = baralho.getFlashcards();

        modelo.setRowCount(0);
        for (Flashcard f : flashcards) {
            modelo.addRow(new Object[]{
                    f.getFrente(),
                    f.getVerso(),
                    f.getProximaRevisao().format(formatador),
                    "Excluir"
            });
        }
    }
}
