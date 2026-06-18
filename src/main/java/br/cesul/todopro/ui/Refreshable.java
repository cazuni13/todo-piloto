package br.cesul.todopro.ui;

// Mesma interface dos outros projetos: cada aba sabe se
// recarregar, e o MainApp chama refresh() quando a aba ganha foco.
public interface Refreshable {
    void refresh();
}
