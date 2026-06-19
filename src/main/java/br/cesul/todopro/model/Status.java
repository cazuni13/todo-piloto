package br.cesul.todopro.model;

// Ciclo de vida de uma tarefa:
//   PENDENTE -> EM_ANDAMENTO -> CONCLUIDA
public enum Status {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDA;

    public String rotulo(){
        return switch (this) {
            case PENDENTE     -> "Pendente";
            case EM_ANDAMENTO -> "Em andamento";
            case CONCLUIDA    -> "Concluída";
        };
    }

    // TODO 2 - implementar a transição de status.
    //
    // Esse método responde: "se a tarefa está neste status e o
    // usuário clica em Avançar, pra qual status ela vai?"
    //
    //     PENDENTE      -> EM_ANDAMENTO
    //     EM_ANDAMENTO  -> CONCLUIDA
    //     CONCLUIDA     -> CONCLUIDA   (não tem pra onde avançar,
    //                                   devolve ela mesma)
    //
    // É um switch igual ao do rotulo(), só que devolvendo
    // constantes do próprio enum em vez de String.
    //
    // Repara que a REGRA do ciclo de vida mora aqui no enum,
    // junto do dado. A UI e o DAO só chamam .proximo() sem
    // saber qual é a ordem - se um dia o ciclo mudar, mexe
    // só aqui.
    public Status proximo() {
        return switch (this) {
            case PENDENTE -> EM_ANDAMENTO;
            case EM_ANDAMENTO -> CONCLUIDA;
            case CONCLUIDA -> CONCLUIDA;
        };
    }
}