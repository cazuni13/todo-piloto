package br.cesul.todopro.ui;

import br.cesul.todopro.dao.TarefaDao;
import br.cesul.todopro.model.Categoria;
import br.cesul.todopro.model.Prioridade;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

// Aba "Nova Tarefa" - formulário de cadastro.
// Esta aba já vem pronta. Ela depende do dao.insert (TODO 3):
// enquanto ele não existir, o botão diz que criou mas nada
// aparece na lista.

// Componente novo aqui: TextArea, que é um TextField de várias
// linhas (bom pra descrição longa).

public class NovaTarefaPane extends BorderPane implements Refreshable {

    private final TarefaDao dao = new TarefaDao();

    private final TextField tTitulo     = new TextField();
    private final TextArea  taDescricao = new TextArea();
    private final ComboBox<Categoria> cCategoria =
            new ComboBox<>(FXCollections.observableArrayList(Categoria.values()));
    private final ComboBox<Prioridade> cPrioridade =
            new ComboBox<>(FXCollections.observableArrayList(Prioridade.values()));
    private final Label lblStatus = new Label();

    public NovaTarefaPane(){
        setPadding(new Insets(15));

        tTitulo.setPromptText("Título");
        taDescricao.setPromptText("Descrição (opcional)");
        taDescricao.setPrefRowCount(4);

        // Valores padrão pro usuário não precisar escolher sempre
        cCategoria.getSelectionModel().select(Categoria.TRABALHO);
        cPrioridade.getSelectionModel().select(Prioridade.MEDIA);

        Button btnSalvar = new Button("Criar tarefa");
        btnSalvar.setOnAction(e -> salvar());

        Button btnLimpar = new Button("Limpar");
        btnLimpar.setOnAction(e -> limpar());

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.addRow(0, new Label("Título:"),     tTitulo);
        form.addRow(1, new Label("Descrição:"),  taDescricao);
        form.addRow(2, new Label("Categoria:"),  cCategoria);
        form.addRow(3, new Label("Prioridade:"), cPrioridade);
        form.addRow(4, btnSalvar, btnLimpar);

        setTop(new VBox(10,
                new Label("Criar uma nova tarefa"),
                form,
                lblStatus));
    }

    private void salvar(){
        String titulo = tTitulo.getText().trim();
        if (titulo.isBlank()){
            lblStatus.setStyle("-fx-text-fill:#c00;");
            lblStatus.setText("Informe um título.");
            return;
        }

        dao.insert(titulo, taDescricao.getText(),
                   cCategoria.getValue(), cPrioridade.getValue());

        lblStatus.setStyle("-fx-text-fill:#0a7f2e;");
        lblStatus.setText("Tarefa \"" + titulo + "\" criada.");
        limpar();
    }

    private void limpar(){
        tTitulo.clear();
        taDescricao.clear();
        cCategoria.getSelectionModel().select(Categoria.TRABALHO);
        cPrioridade.getSelectionModel().select(Prioridade.MEDIA);
    }

    // Aqui não tem lista pra recarregar - só limpamos a mensagem
    // de feedback pra não ficar "tarefa criada" pra sempre.
    @Override
    public void refresh(){
        lblStatus.setText("");
    }
}
