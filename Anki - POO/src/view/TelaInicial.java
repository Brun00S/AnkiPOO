package view;

import model.Baralho;
import controller.AnkiController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static view.CorTema.*;

public class TelaInicial extends JPanel {
    private DefaultListModel<Baralho> modeloLista;
    private JList<Baralho> listaBaralhos;
    private final AnkiController controller;
    private JButton botaoAdicionar, botaoVerCartoes;
    private final Runnable aoAdicionar, aoEstudar, aoVerCartoes;
    private int indiceMouse = -1;

    public TelaInicial(AnkiController controller, Runnable aoAdicionar, Runnable aoEstudar, Runnable aoVerCartoes) {
        this.controller = controller;
        this.aoAdicionar = aoAdicionar;
        this.aoEstudar = aoEstudar;
        this.aoVerCartoes = aoVerCartoes;

        setLayout(new BorderLayout());
        setBackground(FUNDO.get());

        //Cria a parte do titulo (header)
        JLabel titulo = new JLabel("Anki Simples", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 36));
        titulo.setForeground(FONTE.get());
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        //Cria a lista de baralhos a ser exibida na tela inicial
        modeloLista = new DefaultListModel<>();
        listaBaralhos = new JList<>(modeloLista);
        listaBaralhos.setBackground(FUNDO.get());
        listaBaralhos.setForeground(FONTE.get());
        listaBaralhos.setSelectionBackground(FUNDO.get());
        listaBaralhos.setSelectionForeground(FONTE.get());
        listaBaralhos.setCellRenderer(new BaralhoRenderer());


        //Event Listeners para estilizar o hover sobre a lista exibida na tela inicial
        listaBaralhos.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int index = listaBaralhos.locationToIndex(e.getPoint());
                if (index != indiceMouse) {
                    indiceMouse = index;
                    listaBaralhos.repaint();
                }
            }
        });

        listaBaralhos.addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                indiceMouse = -1;
                listaBaralhos.repaint();
            }

            public void mouseClicked(MouseEvent e) {
                int index = listaBaralhos.locationToIndex(e.getPoint());
                Rectangle bounds = listaBaralhos.getCellBounds(index, index);
                if (bounds != null && index != -1) {
                    Point pontoClique = e.getPoint();
                    int xRelativo = pontoClique.x - bounds.x;
                    // Verifica se clicou no botão remover (ex: nos últimos 30px da linha)
                    if (xRelativo > bounds.width - 30) {
                        // Remover baralho pelo índice
                        Baralho removido = modeloLista.getElementAt(index);
                        controller.removerBaralho(removido.getNomeBaralho());
                        atualizarListaBaralho();
                    } else if (e.getClickCount() == 2) {
                        aoEstudar.run();
                    }
                }
            }
        });

        //Cria o scrollpane para a lista de baralhos da tela inicial
        JScrollPane scroll = new JScrollPane(listaBaralhos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(FUNDO.get());
        add(scroll, BorderLayout.CENTER);

        //Criação e configuração dos botões de adicionar flascard e editar baralhos
        botaoAdicionar = new JButton("+");
        botaoAdicionar.setFont(new Font("SansSerif", Font.BOLD, 32));
        botaoAdicionar.setBackground(BOTAO.get());
        botaoAdicionar.setForeground(FONTE.get());
        botaoAdicionar.setFocusPainted(false);
        botaoAdicionar.setPreferredSize(new Dimension(150, 60));
        botaoAdicionar.addActionListener(e -> aoAdicionar.run());

        botaoVerCartoes = new JButton("Cartões");
        botaoVerCartoes.setFont(new Font("SansSerif", Font.BOLD, 24));
        botaoVerCartoes.setBackground(new Color(80, 80, 80));
        botaoVerCartoes.setForeground(FONTE.get());
        botaoVerCartoes.setFocusPainted(false);
        botaoVerCartoes.setPreferredSize(new Dimension(150, 60));
        botaoVerCartoes.addActionListener(e -> aoVerCartoes.run());

        JPanel painelBotao = new JPanel();
        painelBotao.setBackground(FUNDO.get());
        painelBotao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelBotao.add(botaoAdicionar);
        painelBotao.add(botaoVerCartoes);
        add(painelBotao, BorderLayout.SOUTH);

        atualizarListaBaralho();
    }

    // Atualiza a lista do JList com os baralhos do controller
    public void atualizarListaBaralho() {
        modeloLista.clear();
        for (Baralho b : controller.getConjunto().getTodos()) {
            modeloLista.addElement(b);
        }
    }

    //Retorna o nome do baralho selecionado na lista
    public String getBaralhoSelecionado() {
        Baralho b = listaBaralhos.getSelectedValue();
        return b != null ? b.getNomeBaralho() : null;
    }

    /*renderizador customizado dos itens da lista, usado para adicionar as informações de quantos cards
    tem no baralho (em azul), qunatos cards precisam ser revisados (verde) e o botão de eliminar deck (x vemelho)
    * */

    private class BaralhoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Baralho b = (Baralho) value;

            JPanel painel = new JPanel(new BorderLayout());
            painel.setOpaque(true);
            Color fundo = index == indiceMouse && !isSelected ? HOVER.get() : FUNDO.get();
            painel.setBackground(fundo);

            // Título do baralho (esquerda)
            JLabel nomeLabel = new JLabel(b.getNomeBaralho());
            nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            nomeLabel.setForeground(FONTE.get());

            // Informações à direita: total de cards, cards para hoje e ícone remover
            String info = String.format("<html>"
                            + "<span style='color:blue;'>%d cards</span>&nbsp;&nbsp;"
                            + "<span style='color:green;'>%d hoje</span>&nbsp;&nbsp;"
                            + "<span style='color:red;'>[X]</span></html>",
                    b.getTotaldeCards(), b.getNumeroCardsParaHoje());
            JLabel infoLabel = new JLabel(info);
            infoLabel.setForeground(FONTE.get());
            infoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            nomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

            painel.add(nomeLabel, BorderLayout.WEST);
            painel.add(infoLabel, BorderLayout.EAST);

            return painel;
        }
    }
}
