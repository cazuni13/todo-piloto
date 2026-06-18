package br.cesul.todopro.model;

import java.time.LocalDateTime;

// Uma tarefa da lista. Record, igual aos outros projetos.

// Campo novo em relação ao que já fizemos: dataCriacao é um
// LocalDateTime (data + hora). Guardamos como objeto de data de
// verdade (e não String) porque assim dá pra ordenar e comparar
// corretamente. No banco ele vira uma String ISO
// ("2026-06-11T10:32:15") - essa conversão acontece no DAO.

public record Tarefa(
        String id,
        String titulo,
        String descricao,
        Categoria categoria,
        Prioridade prioridade,
        Status status,
        LocalDateTime dataCriacao
) {
    // Atalho pra UI não ficar comparando enum toda hora.
    public boolean concluida(){
        return status == Status.CONCLUIDA;
    }
}
