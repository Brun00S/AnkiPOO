package model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ConjuntoBaralhos {
    private  List<Baralho> baralhos;

    public ConjuntoBaralhos() {
        this.baralhos = new ArrayList<>();
    }

    //retorna todos uma lista com todos baralhos
    public List<Baralho> getTodos() {
        return baralhos;
    }
    //adiciona um baralho a lista
    public void adicionar(Baralho b) {
        baralhos.add(b);
    }
    //remove um baralho da lista
    public void removerBaralho(String nome) {
        baralhos.removeIf(b -> b.getNomeBaralho().equalsIgnoreCase(nome));
    }

    //busca um baralho na lista pelo nome
    public Baralho buscarPorNome(String nome) {
        for (Baralho b : baralhos) {
            if (b.getNomeBaralho().equalsIgnoreCase(nome)) {
                return b;
            }
        }
        return null;
    }

    //salva a lista no arquivo csv que serve como banco de dados
    public void salvarEmArquivo(String caminho) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(caminho))) {
            for (Baralho b : baralhos) {
                for (Flashcard f : b.getFlashcards()) {
                    escritor.printf("%s,%s,%s,%s%n",
                            b.getNomeBaralho(),
                            f.getFrente(),
                            f.getVerso(),
                            f.getProximaRevisao());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //carrega a lista do arquivo
    public static ConjuntoBaralhos carregarDeArquivo(String caminho) {
        ConjuntoBaralhos conjunto = new ConjuntoBaralhos();

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] partes = linha.split(",", 4);
                if (partes.length == 4) {
                    String nome = partes[0];
                    String frente = partes[1];
                    String verso = partes[2];
                    LocalDate data = LocalDate.parse(partes[3]);

                    Baralho baralho = conjunto.buscarPorNome(nome);
                    if (baralho == null) {
                        baralho = new Baralho(nome);
                        conjunto.adicionar(baralho);
                    }

                    baralho.adicionarFlashcard(frente, verso, data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conjunto;
    }
}
