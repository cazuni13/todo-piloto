package br.cesul.todopro.model;

// Categoria de uma tarefa. Enum simples, sem atributo.
// Mesmo padrão da Categoria do quiz e do Genero do catálogo.
public enum Categoria {
    TRABALHO,
    ESTUDOS,
    PESSOAL,
    OUTROS;

    public String rotulo(){
        return switch (this) {
            case TRABALHO -> "Trabalho";
            case ESTUDOS  -> "Estudos";
            case PESSOAL  -> "Pessoal";
            case OUTROS   -> "Outros";
        };
    }
}
