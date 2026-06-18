package br.cesul.todopro.model;

// Prioridade de uma tarefa.

// TODO 1 - transformar este enum em um enum RICO.
//
// Hoje ele é um enum "pobre": só constantes, e os getters
// embaixo retornam valores fixos provisórios. Vocês vão fazer cada
// constante carregar dois valores:
//
//     peso    -> usado pra ordenar (ALTA mais importante)
//     corCss  -> cor de fundo da linha na tabela
//
// Valores que cada constante deve ter:
//     BAIXA -> peso 1, cor "#c9f5c9"  (verde claro)
//     MEDIA -> peso 2, cor "#fff3b0"  (amarelo)
//     ALTA  -> peso 3, cor "#f5c9c9"  (vermelho claro)
//
// O caminho é o mesmo da Dificuldade do quiz battle (que carregava
// os pontos). Relembrando os passos:
// 1 - colocar os argumentos nas constantes:  BAIXA(1, "#c9f5c9"), ...
// 2 - declarar os dois atributos (private final)
// 3 - escrever o construtor que recebe os dois e guarda
//     (lembra: construtor de enum é implicitamente privado,
//      só a JVM chama, uma vez pra cada constante)
// 4 - fazer os getters de verdade (retornando os atributos)
//
// Quando terminar, rode o app: as linhas da tabela vão ganhar
// cor sozinhas (a tela já tá pronta esperando esses getters).

public enum Prioridade {
    BAIXA,
    MEDIA,
    ALTA;

    // provisório - trocar pelo getter do atributo
    public int getPeso(){
        return 0;
    }

    // provisório - trocar pelo getter do atributo
    public String getCorCss(){
        return "#ffffff";
    }

    public String rotulo(){
        return switch (this) {
            case BAIXA -> "Baixa";
            case MEDIA -> "Média";
            case ALTA  -> "Alta";
        };
    }
}
