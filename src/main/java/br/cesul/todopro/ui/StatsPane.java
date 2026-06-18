package br.cesul.todopro.ui;

import br.cesul.todopro.dao.TarefaDao;

import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Aba "Estatísticas" - dois PieCharts lado a lado:
//   esquerda: tarefas por status
//   direita:  tarefas por prioridade

public class StatsPane extends BorderPane implements Refreshable {

    private final TarefaDao dao = new TarefaDao();

    private final PieChart chartStatus     = new PieChart();
    private final PieChart chartPrioridade = new PieChart();

    public StatsPane(){
        setPadding(new Insets(10));

        chartStatus.setTitle("Por status");
        chartPrioridade.setTitle("Por prioridade");
        chartStatus.setLabelsVisible(true);
        chartPrioridade.setLabelsVisible(true);

        HBox charts = new HBox(16, chartStatus, chartPrioridade);
        setCenter(new VBox(new Label("Estatísticas das tarefas"), charts));

        refresh();
    }

    @Override
    public void refresh(){
        // os gráficos são preenchidos mais pra frente
    }
}
