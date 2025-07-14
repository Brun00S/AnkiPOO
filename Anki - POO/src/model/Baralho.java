package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho {
    private String nomeBaralho;
    private List<Flashcard> flashcards;
    private int totaldeCards;
    private int numeroCardsParaHoje;

    public Baralho(String nomeBaralho) {
        this.nomeBaralho = nomeBaralho;
        this.flashcards = new ArrayList<>();
        this.totaldeCards = 0;
        this.numeroCardsParaHoje = 0;
    }

    //getters

    public String getNomeBaralho() {
        return nomeBaralho;
    }

    public int getTotaldeCards() {
        return totaldeCards;
    }

    public int getNumeroCardsParaHoje() {
        return numeroCardsParaHoje;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    //Decrementa o número de cards que é para revisar no dia atual

    public void decrementarCardsParaHoje() {
        if (numeroCardsParaHoje > 0) {
            numeroCardsParaHoje--;
        }
    }


    //adiciona um flashcard ao baralho
    public void adicionarFlashcard(String frente, String verso, LocalDate data) {
        Flashcard novo = new Flashcard(frente, verso, data, nomeBaralho);
        flashcards.add(novo);
        totaldeCards++;

        if (!data.isAfter(LocalDate.now())) {
            numeroCardsParaHoje++; // incrementa se o card é para hoje ou antes
        }
    }

    //remove um flashcard do baralho
    public void removerFlashcard(Flashcard card) {
        flashcards.remove(card);
        totaldeCards--;
        if (!card.getProximaRevisao().isAfter(LocalDate.now())) {
            numeroCardsParaHoje--;
        }
    }


    //retorna uma lista de flaschards já embaralhados que devem ser revisados naquele dia
    public List<Flashcard> filtrarParaHoje(String deck) {
        List<Flashcard> resultado = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        numeroCardsParaHoje = 0;
        for (Flashcard f : flashcards) {
            if (f.getNomeDeck().equals(deck) && !f.getProximaRevisao().isAfter(hoje)) {
                resultado.add(f);
                numeroCardsParaHoje++;
            }
        }
        Collections.shuffle(resultado);
        return resultado;
    }




}
