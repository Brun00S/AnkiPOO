package view;

import controller.AnkiController;
import model.Baralho;
import model.ConjuntoBaralhos;

import javax.swing.*;
import java.awt.*;

public class AnkiGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;

    private ConjuntoBaralhos conjunto;
    private TelaInicial telaInicial;
    private TelaCadastroBaralho telaCadastro;
    private TelaEstudo telaEstudo;
    private TelaCartoesAdicionados telaCartoes;

    public AnkiGUI() {
        setTitle("Anki POO");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        conjunto = ConjuntoBaralhos.carregarDeArquivo("flashcards.csv");

        AnkiController controller = new AnkiController(conjunto);


        //Criação das telas em duas etapas
        // Etapa 1: cria instâncias com null temporários
        telaInicial = new TelaInicial(controller, null, null, null);
        telaCadastro = new TelaCadastroBaralho(controller, null);
        telaEstudo = new TelaEstudo(controller, null, null);
        telaCartoes = new TelaCartoesAdicionados(controller, null);

        // Etapa 2: configura os Runnables corretamente
        telaInicial = new TelaInicial(
                controller,
                () -> cardLayout.show(container, "cadastro"),
                () -> {
                    Baralho b = conjunto.buscarPorNome(telaInicial.getBaralhoSelecionado());
                    if (b != null) {
                        telaEstudo.setBaralhoSelecionado(b);
                        cardLayout.show(container, "estudo");
                    }
                },
                () -> {
                    telaCartoes.atualizarConteudo();
                    cardLayout.show(container, "cartoes");
                }
        );

        telaCadastro = new TelaCadastroBaralho(controller, () -> {
            telaInicial.atualizarListaBaralho();
            telaCartoes.atualizarConteudo();
            cardLayout.show(container, "inicial");
        });

        telaEstudo = new TelaEstudo(
                controller,
                () -> cardLayout.show(container, "inicial"),
                () -> telaInicial.atualizarListaBaralho()
        );

        telaCartoes = new TelaCartoesAdicionados(
                controller,
                () -> cardLayout.show(container, "inicial")
        );

        //Seta as telas da app pro controller as acesse
        controller.setTelas(telaInicial, telaCartoes, telaCadastro, telaEstudo);

        // Adiciona ao container
        container.add(telaInicial, "inicial");
        container.add(telaCadastro, "cadastro");
        container.add(telaEstudo, "estudo");
        container.add(telaCartoes, "cartoes");

        setContentPane(container);
        cardLayout.show(container, "inicial");
        setVisible(true);
    }



}
