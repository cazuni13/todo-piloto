package br.cesul.todopro.ui;

import br.cesul.todopro.dao.TarefaDao;
import br.cesul.todopro.model.Categoria;
import br.cesul.todopro.model.Prioridade;
import br.cesul.todopro.model.Status;
import br.cesul.todopro.model.Tarefa;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.time.format.DateTimeFormatter;

// Aba "Tarefas": a tela principal do app.
//
// Layout:
//   topo:   3 combos de filtro + botão limpar
//   centro: tabela (com a linha colorida pela prioridade)
//   rodapé: botões Avançar status / Excluir
//
// A tela já tá toda montada. O que falta de vocês aqui são os
// dois handlers do rodapé (avançar e excluir).

public class TarefasPane extends BorderPane implements Refreshable {

    private final TarefaDao dao = new TarefaDao();

    private final ObservableList<Tarefa> data = FXCollections.observableArrayList();
    private final TableView<Tarefa> table = new TableView<>(data);

    // Combos de filtro. Ficam como atributo porque o refresh()
    // precisa ler o valor deles.
    private final ComboBox<Status>     cboStatus     = new ComboBox<>();
    private final ComboBox<Prioridade> cboPrioridade = new ComboBox<>();
    private final ComboBox<Categoria>  cboCategoria  = new ComboBox<>();

    // Formatador da data pra coluna "Criada em" (11/06 14:30).
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM HH:mm");

    public TarefasPane(){
        setPadding(new Insets(10));

        // ===== TOPO: filtros =====

        // Truque do combo de filtro: a primeira opção é null, que
        // significa "sem filtro".
        cboStatus.getItems().add(null);
        cboStatus.getItems().addAll(Status.values());

        cboPrioridade.getItems().add(null);
        cboPrioridade.getItems().addAll(Prioridade.values());

        cboCategoria.getItems().add(null);
        cboCategoria.getItems().addAll(Categoria.values());

        cboStatus.setPromptText("Status");
        cboPrioridade.setPromptText("Prioridade");
        cboCategoria.setPromptText("Categoria");

        // Qualquer mudança em qualquer combo -> recarrega a tabela.
        cboStatus.setOnAction(e -> refresh());
        cboPrioridade.setOnAction(e -> refresh());
        cboCategoria.setOnAction(e -> refresh());

        Button btnLimpar = new Button("Limpar filtros");
        btnLimpar.setOnAction(e -> {
            cboStatus.setValue(null);
            cboPrioridade.setValue(null);
            cboCategoria.setValue(null);
            refresh();
        });

        HBox topo = new HBox(8,
                new Label("Filtros:"),
                cboStatus, cboPrioridade, cboCategoria,
                btnLimpar);
        topo.setPadding(new Insets(0, 0, 8, 0));
        setTop(topo);

        // ===== CENTRO: tabela =====

        table.setPlaceholder(new Label("Nenhuma tarefa encontrada"));

        TableColumn<Tarefa, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setMinWidth(220);
        colTitulo.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().titulo()));

        TableColumn<Tarefa, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setMinWidth(100);
        colCategoria.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().categoria().rotulo()));

        TableColumn<Tarefa, String> colPrioridade = new TableColumn<>("Prioridade");
        colPrioridade.setMinWidth(100);
        colPrioridade.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().prioridade().rotulo()));

        TableColumn<Tarefa, String> colStatus = new TableColumn<>("Status");
        colStatus.setMinWidth(110);
        colStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().status().rotulo()));

        TableColumn<Tarefa, String> colCriada = new TableColumn<>("Criada em");
        colCriada.setMinWidth(120);
        colCriada.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().dataCriacao().format(FMT)));

        table.getColumns().add(colTitulo);
        table.getColumns().add(colCategoria);
        table.getColumns().add(colPrioridade);
        table.getColumns().add(colStatus);
        table.getColumns().add(colCriada);

        // Pinta a LINHA INTEIRA conforme a prioridade da tarefa.
        // O rowFactory é o "primo" do cellFactory: em vez de uma
        // célula, ele controla a linha. Pra cada linha que aparece,
        // o JavaFX chama updateItem e a gente decide o estilo.
        //
        // Enquanto o enum Prioridade de vocês estiver com o getter
        // provisório (#ffffff), as linhas ficam brancas. Quando o
        // TODO 1 estiver pronto, isso aqui ganha vida sozinho.
        table.setRowFactory(tv -> new TableRow<Tarefa>(){
            @Override
            protected void updateItem(Tarefa item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null){
                    setStyle("");
                } else if (item.concluida()){
                    // concluída fica cinza, independente da prioridade
                    setStyle("-fx-background-color:#e0e0e0;");
                } else {
                    setStyle("-fx-background-color:" + item.prioridade().getCorCss() + ";");
                }
            }
        });

        setCenter(table);

        // ===== RODAPÉ: ações =====

        Button btnAvancar = new Button("Avançar status");
        btnAvancar.setOnAction(e -> avancar());     // TODO 6

        Button btnExcluir = new Button("Excluir selecionada");
        btnExcluir.setOnAction(e -> excluir());     // TODO 7

        setBottom(new ToolBar(btnAvancar, btnExcluir));

        refresh();
    }

    // TODO 6 - avançar o status da tarefa selecionada.
    // Mesmo padrão do excluir do catálogo:
    // 1 - pegar a selecionada (table.getSelectionModel().getSelectedItem())
    // 2 - se null, sai
    // 3 - dao.avancarStatus(sel.id())
    // 4 - refresh()
    private void avancar(){
        // TODO
        Tarefa sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        dao.avancarStatus(sel.id());
        refresh();
    }

    // TODO 7 - excluir a tarefa selecionada.
    // Igual ao de cima trocando a chamada por dao.delete(sel.id()).
    private void excluir(){
        // TODO
        Tarefa sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        dao.delete(sel.id());
        refresh();
    }

    // Recarrega a tabela com o que o dao.findFiltrado devolve.
    @Override
    public void refresh(){
        data.setAll(dao.findFiltrado(
                cboStatus.getValue(),
                cboPrioridade.getValue(),
                cboCategoria.getValue()
        ));
    }
}
