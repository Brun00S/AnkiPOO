package model;

import java.time.LocalDate;

public class Flashcard {
    private String Frente;
    private String Verso;
    private LocalDate proximaRevisao;
    private String nomeDeck;

    public Flashcard(String Frente, String Verso, LocalDate proximaRevisao, String nomeDeck) {
        this.Frente = Frente;
        this.Verso = Verso;
        this.proximaRevisao = proximaRevisao;
        this.nomeDeck = nomeDeck;
    }


    //getters e setters
    public String getFrente() {
        return Frente;
    }

    public String getVerso() {
        return Verso;
    }

    public LocalDate getProximaRevisao() {
        return proximaRevisao;
    }

    public void setProximaRevisao(LocalDate proximaRevisao) {
        this.proximaRevisao = proximaRevisao;
    }

    public String getNomeDeck() {
        return nomeDeck;
    }

}
