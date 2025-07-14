package controller;

import model.Baralho;
import model.ConjuntoBaralhos;
import model.Flashcard;
import view.*;

public class AnkiController {

    private final ConjuntoBaralhos conjunto;
    private TelaInicial telaInicial;
    private TelaCartoesAdicionados telaCartoesAdicionados;
    private TelaEstudo telaEstudo;
    private TelaCadastroBaralho telaCadastroBaralho;
    private final String caminhoArquivo = "flashcards.csv";

    public AnkiController(ConjuntoBaralhos conjunto) {
        this.conjunto = conjunto;
    }

    public void setTelas(TelaInicial inicial, TelaCartoesAdicionados cartoes, TelaCadastroBaralho cadastroBaralho, TelaEstudo estudo ) {
        this.telaInicial = inicial;
        this.telaCartoesAdicionados = cartoes;
        this.telaCadastroBaralho = cadastroBaralho;
        this.telaEstudo = estudo;
    }

    //Adiciona um baralho no conjunto de baralhos
    public void adicionarBaralho(String nome) {
        if (conjunto.buscarPorNome(nome) == null) {
            conjunto.adicionar(new Baralho(nome));
            salvar();
            atualizarViews();
        }
    }

    public void removerBaralho(String nome) {
        conjunto.removerBaralho(nome);
        salvar();
        atualizarViews();
    }

    public void adicionarFlashcard(String baralhoNome, Flashcard f) {
        Baralho b = conjunto.buscarPorNome(baralhoNome);
        if (b != null) {
            b.adicionarFlashcard(f.getFrente(), f.getVerso(), f.getProximaRevisao());
            salvar();
            atualizarViews();
        }
    }

    public void removerFlashcard(String baralhoNome, Flashcard f) {
        Baralho b = conjunto.buscarPorNome(baralhoNome);
        if (b != null) {
            b.removerFlashcard(f);
            salvar();
            atualizarViews();
        }
    }

    public ConjuntoBaralhos getConjunto() {
        return conjunto;
    }

    public void salvar() {
        conjunto.salvarEmArquivo(caminhoArquivo);
    }

    private void atualizarViews() {
        if (telaInicial != null) telaInicial.atualizarListaBaralho();
        if (telaCartoesAdicionados != null) telaCartoesAdicionados.atualizarConteudo();
        if (telaCadastroBaralho != null) telaCadastroBaralho.atualizarDropdown();
    }



}
