package br.cesul.todopro.dao;

// DAO da coleção 'tarefas'. Mesmo papel de sempre: a UI nunca
// fala com o mongo, fala com esta classe.

// Vocês recebem o toTarefa e o findAll prontos. O resto é TODO.

import br.cesul.todopro.model.Categoria;
import br.cesul.todopro.model.Prioridade;
import br.cesul.todopro.model.Status;
import br.cesul.todopro.model.Tarefa;
import br.cesul.todopro.util.MongoConfig;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class TarefaDao {

    private final MongoCollection<Document> col = MongoConfig.tarefas();

    // Conversão Document -> Tarefa.
    // Novidade: o campo dataCriacao tá guardado no banco como String
    // ISO ("2026-06-11T10:32:15"). O LocalDateTime.parse lê essa
    // String e devolve o objeto de data. Essa conversão fica AQUI no
    // DAO - o model e a UI nem ficam sabendo como o banco guarda.
    private Tarefa toTarefa(Document d){
        return new Tarefa(
                d.getObjectId("_id").toHexString(),
                d.getString("titulo"),
                d.getString("descricao"),
                Categoria.valueOf(d.getString("categoria")),
                Prioridade.valueOf(d.getString("prioridade")),
                Status.valueOf(d.getString("status")),
                LocalDateTime.parse(d.getString("dataCriacao"))
        );
    }

    public List<Tarefa> findAll(){
        List<Tarefa> list = new ArrayList<>();
        for (Document d : col.find()){
            list.add(toTarefa(d));
        }
        return list;
    }

    // TODO 3 - inserir uma tarefa nova.
    // Já fizeram insert duas vezes (quiz e catálogo), a diferença
    // aqui são dois campos que o usuário NÃO digita:
    //   status      -> toda tarefa nasce PENDENTE
    //                  (grava Status.PENDENTE.name())
    //   dataCriacao -> agora
    //                  (grava LocalDateTime.now().toString())
    // Não esquece de validar o título em branco antes.
    public void insert(String titulo, String descricao,
                       Categoria cat, Prioridade pri){
        // TODO
        if (titulo == null || titulo.isBlank()) {
            return;
        }

        Document novaTarefa = new Document()
                .append("titulo", titulo)
                .append("descricao", descricao)
                .append("categoria", cat.name())
                .append("prioridade", pri.name())
                .append("status", Status.PENDENTE.name())
                .append("dataCriacao", LocalDateTime.now().toString());

        col.insertOne(novaTarefa);
    }

    // TODO 4 - apagar uma tarefa.
    // Igualzinho ao delete do catálogo: col.deleteOne com
    // eq("_id", new ObjectId(id)).
    public void delete(String id) {
        // TODO
        if (id == null || id.isBlank()) {
            return;
        }

        col.deleteOne(eq("_id", new ObjectId(id)));
    }


    // Busca de tarefas. Por enquanto devolve todas; os filtros entram
    // mais pra frente.
    public List<Tarefa> findFiltrado(Status st, Prioridade pri, Categoria cat){
        return findAll();
    }

    // TODO 5 - avançar o status de uma tarefa.
    //
    // Esse update é diferente dos que já fizemos: o valor novo
    // DEPENDE do valor atual. Então o método tem duas etapas:
    //
    // 1 - LER o documento atual:
    //         Document d = col.find(eq("_id", new ObjectId(id))).first();
    //     (first() devolve o primeiro resultado, ou null se não achou
    //      - trata o null!)
    //
    // 2 - calcular e GRAVAR o novo status:
    //         pega o status atual:  Status.valueOf(d.getString("status"))
    //         calcula o próximo:    atual.proximo()   <- TODO 2 de vocês
    //         grava com updateOne + set("status", novo.name())
    public void avancarStatus(String id){
        // TODO
        if (id == null || id.isBlank()) {
            return;
        }

        Document d = col.find(eq("_id", new ObjectId(id))).first();


        if (d != null) {
            Status atual = Status.valueOf(d.getString("status"));
            Status novoStatus = atual.proximo();
            col.updateOne(
                    eq("_id", new ObjectId(id)),
                    set("status", novoStatus.name())
            );
        }
    }

    // Contagem de tarefas por status. Por enquanto devolve vazio.
    public Map<String, Long> countByStatus(){
        return new HashMap<>();
    }

    // Contagem de tarefas por prioridade. Por enquanto devolve vazio.
    public Map<String, Long> countByPrioridade(){
        return new HashMap<>();
    }
}
