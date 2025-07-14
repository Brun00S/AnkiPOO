# ⭐ Anki Simples - Aplicativo de Flashcards com Revisão Espaçada

> Um sistema de revisão de conteúdos baseado em flashcards e espaçamento de revisão.
> Desenvolvido como trabalho prático e objeto de avaliação parcial para a disciplina MATA55 Programação Orientada a Objetos (POO), do Instituto de Computação - UFBA, semestre 2025.1.

---

## 💻 Sobre o Projeto

**Anki Simples** Anki Simples é uma aplicação desktop feita em Java com Swing que simula um sistema de flashcards, inspirado no popular Anki.
> A Aplicação permite a criação de baralhos, cadastro de cartões, revisão de conteúdos com espaçamento baseado em desempenho, e organização intuitiva via interface gráfica.
> O objetivo é através da interface criar uma rotina de estudo baseada em flashcards e no conceito de memorização por repetição espaçada
> Essa versão do Anki suporta apenas **um** tipo de flashcard, que é um flashcard de texto contendo frente e verso. A frente é a informação inicial fornecida ao estudante e o verso é a informação que o estudante deve lembrar.

---

## 🎯 Objetivos do Trabalho

- Aplicar os conceitos de **Programação Orientada a Objetos (POO)** na prática.
- Separar de forma clara as responsabilidades entre modelo, visão e controle (MVC).
- Criar um software educativo funcional e com design amigável.
- Gerenciar persistência dos dados com arquivos CSV.
- Reforçar conceitos como composição, encapsulamento e separação de responsabilidades

---

## 🧠 Conceitos

| Conceito           | Como foi aplicado                                                                 |
|--------------------|------------------------------------------------------------------------------------|
| **Organização**    | Estrutura em pacotes como `model/`, `view/`, `controller/`, com responsabilidades separadas  |
| **Encapsulamento** | Atributos privados, uso de getters e setters(quando necessário) métodos de controle de acesso        |
| **Composição**     | `ConjuntoBaralhos` possui múltiplos `Baralhos` que por sua vez pode possuir múltiplos  `Flashcard`                    |
| **Agregação**      | `AnkiController` coordena  `ConjuntoBaralhos`, mas `ConjuntoBaralhos`tem ciclo de vida independente  |
| **Herança**     | Sem necessidade pela simplicidade do projeto, mas é possível de ser incluído posteriormente ao adicionar tipos diferentes de flashcard)            |
| **Animações**      | Animação ao piscar dos botões implementada com `AnimatedContainer`                |
| **MVC**| Compatível com Android, Windows e outros através do Flutter                       |

---

## 🧩 Telas e Funcionalidades

| Tela               | Descrição                                   |
|--------------------|---------------------------------------------|
| Tela Inicial       | Lista de baralhos criados, opção de adicionar/remover e iniciar revisão   |
| Tela de Cadastro      | Cadastro de novos flashcards e baralhos     |
| Tela de Estudo        | Apresenta cartões para revisão, com opções de dificuldade (ruim, bom, fácil)          |
| Tela de Cartões Adicionados   | Lista os flashcards existentes por baralho, com opção de exclusão              |

---

##  Tela Inicial

> A Tela Inicial é o ponto de entrada do usuário no sistema. Ela exibe uma lista de baralhos criados, mostrando o nome de cada baralho junto com informações importantes, como a quantidade total de cartões contidos e quantos desses cartões estão programados para revisão naquele dia. A lista permite seleção de um baralho para iniciar o estudo ou para exclusão, por meio de uma interface intuitiva que inclui botões visíveis para adicionar novos baralhos ou acessar os cartões existentes. A interação permite ainda o duplo clique sobre um baralho para iniciar o estudo imediatamente.
> A Tela Inicial apresenta a lista de baralhos disponíveis para estudo, com as seguintes funcionalidades e informações visuais:

- **Lista de Baralhos:** Cada item mostra:
  - **Nome do Baralho** (em destaque).
  - **Números coloridos indicando:**
    - **Azul:** Quantidade total de cartões no baralho.
    - **Verde:** Quantidade de cartões para revisão no dia atual.
    - **Vermelho:** Ícone "[X]" para remover o baralho.

- **Interações:**
  - **Clique no "[X]" vermelho:** Remove o baralho correspondente.
  - **Duplo clique em um baralho:** Inicia a sessão de estudo dos cartões daquele baralho.

- **Botões na parte inferior:**
  - **“+” (Adicionar):** Abre a tela para adicionar novos cartões ou baralhos.
  - **“Cartões”:** Leva para a tela que lista todos os cartões cadastrados.


## Tela de Cadastro

> A Tela de Estudo é a interface destinada à revisão dos cartões para o dia atual. Nela, o usuário visualiza a frente do cartão, podendo optar por revelar o verso para conferir o conteúdo completo. Após visualizar o verso, o usuário pode avaliar o nível de domínio daquele cartão selecionando entre opções que refletem o grau de facilidade em recordar a informação — as opções influenciam o intervalo para a próxima revisão do cartão. A interface é projetada para garantir foco na revisão, exibindo apenas um cartão por vez e controlando o fluxo até que todos os cartões programados para o dia sejam revisados.

Esta tela contém um formulário para adicionar novos cartões, com os seguintes elementos e botões:

- **Dropdown “Tipo”:** Por enquanto, disponível apenas o tipo "Texto".
- **Dropdown “Baralho”:** Seleciona o baralho ao qual o cartão será adicionado.
- **Campo “Frente”:** Texto da frente do cartão.
- **Campo “Verso”:** Texto do verso do cartão.

- **Botões:**
  - **“Adicionar Baralho”:** Abre um diálogo para criar um novo baralho, solicitando o nome.
  - **“Salvar”:** Salva o cartão criado, validando se todos os campos foram preenchidos.
  - **“Cancelar”:** Cancela a operação e retorna à tela anterior sem salvar.
  
##  Tela de Estudo

> A Tela de Estudo é a interface destinada à revisão dos cartões para o dia atual. Nela, o usuário visualiza a frente do cartão, podendo optar por revelar o verso para conferir o conteúdo completo. Após visualizar o verso, o usuário pode avaliar o nível de domínio daquele cartão selecionando entre opções que refletem o grau de facilidade em recordar a informação — as opções influenciam o intervalo para a próxima revisão do cartão. A interface é projetada para garantir foco na revisão, exibindo apenas um cartão por vez e controlando o fluxo até que todos os cartões programados para o dia sejam revisados.

Esta tela é dedicada à revisão dos cartões selecionados, com os seguintes botões e funcionalidades:

- **Área de texto:** Exibe a frente do cartão inicialmente e, ao revelar, mostra também o verso.

- **Botões:**
  - **“Revelar”:** Mostra o verso do cartão atual e ativa os botões de avaliação.
  - **“Ruim”:** Avalia o conhecimento como baixo; agenda nova revisão em 1 dia.
  - **“Bom”:** Avaliação intermediária; agenda revisão em 2 dias.
  - **“Fácil”:** Avaliação positiva; agenda revisão em 5 dias.
  - **“Voltar”:** Encerra a sessão de estudo e retorna à tela inicial.

Os botões de avaliação só ficam visíveis após o conteúdo do verso ser revelado, garantindo uma interação organizada.


## Tela de Cartões Adicionados
> Esta tela lista todos os cartões cadastrados no sistema, organizados por baralho. Oferece recursos para navegação e visualização detalhada dos cartões, além de permitir a remoção individual de cartões indesejados. Um menu suspenso (dropdown) possibilita a filtragem dos cartões por baralho, facilitando a localização dos cartões específicos
> Permite a visualização e gerenciamento de todos os cartões cadastrados, organizados por baralho:

- **Dropdown de Baralhos:** Filtra os cartões exibidos na tabela pelo baralho selecionado.

- **Tabela de Cartões:** Lista cartões com colunas para frente, verso, data da próxima revisão e um botão “Remover”.

- **Botões:**
  - **“Remover” (por cartão):** Exclui o cartão selecionado.
  - **“Voltar”:** Retorna à tela anterior, geralmente a Tela Inicial.



---

### 🙋 Informações Acadêmicas
- Discente: Bruno Santos e Santos
- Curso: Licenciatura em Computação
- Disciplina: MATA55 - Programação Orientada a Objetos (POO)
- Professor: Rodrigo Rocha Gomes e Souza
