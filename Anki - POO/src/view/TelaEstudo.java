package view;

import model.Baralho;
import model.Flashcard;
import controller.AnkiController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static view.CorTema.*;

public class TelaEstudo extends JPanel {
    private JTextArea areaTexto;
    private JButton revelarButton, ruimButton, bomButton, facilButton, voltarButton;
    private JPanel painelBotoes;

    private List<Flashcard> cardsHoje;
    private Iterator<Flashcard> iterator;
    private Flashcard atual;
    private Baralho baralhoSelecionado;
    private AnkiController controller;
    private Runnable aoVoltar;
    private Runnable aoAtualizarLista;

    public TelaEstudo(AnkiController controller, Runnable aoVoltar, Runnable aoAtualizarLista) {
        this.controller = controller;
        this.aoVoltar = aoVoltar;
        this.aoAtualizarLista = aoAtualizarLista;

        setLayout(new BorderLayout());
        setBackground(FUNDO.get());

        //Criação e configuração da exibição do card
        areaTexto = new JTextArea();
        areaTexto.setFont(new Font("Serif", Font.BOLD, 24));
        areaTexto.setForeground(FONTE.get());
        areaTexto.setBackground(FUNDO.get());
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setEditable(false);
        areaTexto.setOpaque(false);
        areaTexto.setMargin(new Insets(100, 300, 100, 50));
        areaTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(FUNDO.get());
        scrollPane.getViewport().setBackground(FUNDO.get());

        //Criação e configuração dos botões revelar, ruim, bom, fácil e volta
        //Não reunidos em uma função única pois tem configurações muito diversas entre si
        revelarButton = new JButton("Revelar");
        revelarButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        revelarButton.setBackground(BOTAO.get());
        revelarButton.setForeground(FONTE.get());
        revelarButton.setFocusPainted(false);
        revelarButton.setPreferredSize(new Dimension(150, 30));
        revelarButton.addActionListener(e -> revelarVerso());

        ruimButton = new JButton("Ruim");
        ruimButton.setBackground(new Color(220, 53, 69));
        ruimButton.setForeground(Color.WHITE);
        ruimButton.setPreferredSize(new Dimension(140, 40));
        ruimButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        ruimButton.addActionListener(e -> avaliarFlashcard(1));

        bomButton = new JButton("Bom");
        bomButton.setBackground(new Color(40, 167, 69));
        bomButton.setForeground(Color.WHITE);
        bomButton.setPreferredSize(new Dimension(140, 40));
        bomButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        bomButton.addActionListener(e -> avaliarFlashcard(2));

        facilButton = new JButton("Fácil");
        facilButton.setBackground(new Color(0, 123, 255));
        facilButton.setForeground(Color.WHITE);
        facilButton.setPreferredSize(new Dimension(140, 40));
        facilButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        facilButton.addActionListener(e -> avaliarFlashcard(5));

        voltarButton = new JButton("Voltar");
        voltarButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        voltarButton.setBackground(new Color(120, 120, 120));
        voltarButton.setForeground(Color.WHITE);
        voltarButton.setPreferredSize(new Dimension(100, 30));
        voltarButton.addActionListener(e -> aoVoltar.run());

        //Criação dos paineis e adição dos botões aos paineis
        painelBotoes = new JPanel();
        painelBotoes.setBackground(FUNDO.get());
        painelBotoes.add(ruimButton);
        painelBotoes.add(bomButton);
        painelBotoes.add(facilButton);
        painelBotoes.setVisible(false);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FUNDO.get());
        centro.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        centro.add(scrollPane, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelInferior.setBackground(FUNDO.get());
        painelInferior.add(revelarButton);
        painelInferior.add(voltarButton);

        centro.add(painelInferior, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }


    //Configura o baralho que é utilizado na revisão de flaschards do dia
    public  void setBaralhoSelecionado(Baralho baralho) {
        this.baralhoSelecionado = baralho;
        this.cardsHoje = baralhoSelecionado.filtrarParaHoje(baralhoSelecionado.getNomeBaralho());
        this.iterator = cardsHoje.iterator();

        if (cardsHoje.isEmpty()) {
            areaTexto.setText("Sem mais cards para revisar por hoje.");
            painelBotoes.setVisible(false);
            revelarButton.setVisible(false);
        } else {
            revelarButton.setVisible(true);
            proximoFlashcard();
        }
    }

    private void revelarVerso() {
        if (atual != null) {
            areaTexto.setText(atual.getFrente() + "\n\n--------------------------\n\n" + atual.getVerso());
            painelBotoes.setVisible(true);
        }
    }

    //Função usada nos botões como forma de aplicar a lógica do Anki de repetição espaçada
    private void avaliarFlashcard(int dias) {
        if (atual != null) {
            atual.setProximaRevisao(LocalDate.now().plusDays(dias));
            controller.salvar();  // salvar via controller
            baralhoSelecionado.decrementarCardsParaHoje();
            if (aoAtualizarLista != null) aoAtualizarLista.run();
            proximoFlashcard();
        }
    }

    //Muda as informações na tela para o ŕpximo flashcards na lista de revisão do dia
    private void proximoFlashcard() {
        if (iterator.hasNext()) {
            atual = iterator.next();
            areaTexto.setText(atual.getFrente());
            painelBotoes.setVisible(false);
            revelarButton.setVisible(true);
        } else {
            areaTexto.setText("Fim da revisão de hoje!");
            painelBotoes.setVisible(false);
            revelarButton.setVisible(false);
        }
    }
}
