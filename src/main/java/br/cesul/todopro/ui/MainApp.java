package br.cesul.todopro.ui;

// Ponto de entrada. Estrutura idêntica à do catálogo:
// 3 abas + listener que dá refresh na aba que ganhou foco.

import br.cesul.todopro.util.MongoConfig;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        MongoConfig.seedTarefasIfEmpty();

        TabPane tabs = new TabPane(
                abaNaoFechavel("Tarefas",       new TarefasPane()),
                abaNaoFechavel("Nova Tarefa",   new NovaTarefaPane()),
                abaNaoFechavel("Estatísticas",  new StatsPane())
        );

        // Refresh automático na troca de aba (mesmo padrão do catálogo).
        tabs.getSelectionModel().selectedItemProperty()
            .addListener((obs, antiga, nova) -> {
                if (nova == null) return;
                Node conteudo = nova.getContent();
                if (conteudo instanceof Refreshable) {
                    ((Refreshable) conteudo).refresh();
                }
            });

        stage.setScene(new Scene(tabs, 760, 520));
        stage.setTitle("To-Do Pro");
        stage.show();
    }

    private Tab abaNaoFechavel(String titulo, Node conteudo){
        Tab t = new Tab(titulo, conteudo);
        t.setClosable(false);
        return t;
    }

    public static void main(String[] args){
        launch(args);
    }
}
