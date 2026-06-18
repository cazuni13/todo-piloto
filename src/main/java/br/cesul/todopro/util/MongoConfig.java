package br.cesul.todopro.util;

// Mesmo padrão dos outros projetos: conexão única + helper da
// coleção + seed pra ter dados na primeira execução.

// Banco: todo_pro / Coleção: tarefas

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Arrays;

public final class MongoConfig {

    private static final MongoClient CLIENT =
            MongoClients.create("mongodb://localhost:27017");

    public static MongoCollection<Document> tarefas(){
        return CLIENT.getDatabase("todo_pro").getCollection("tarefas");
    }

    // Seed: só insere se a coleção estiver vazia.
    // Tem tarefa de todo status/prioridade/categoria de propósito,
    // pra vocês conseguirem testar os filtros e as estatísticas
    // antes mesmo do insert de vocês funcionar.
    public static void seedTarefasIfEmpty(){
        if (tarefas().countDocuments() > 0) return;

        tarefas().insertMany(Arrays.asList(
                nova("Estudar pra prova de Paradigmas", "Revisar records, enums e Mongo",
                        "ESTUDOS", "ALTA",  "EM_ANDAMENTO", 5),
                nova("Entregar relatório do estágio",   "",
                        "TRABALHO", "ALTA",  "PENDENTE",     3),
                nova("Responder e-mails atrasados",     "",
                        "TRABALHO", "MEDIA", "PENDENTE",     2),
                nova("Agendar dentista",                "Ligar no período da manhã",
                        "PESSOAL",  "BAIXA", "PENDENTE",     8),
                nova("Lavar o carro",                   "",
                        "PESSOAL",  "BAIXA", "CONCLUIDA",    10),
                nova("Refazer exercício do PetShop",    "Foco em herança e interface",
                        "ESTUDOS",  "MEDIA", "CONCLUIDA",    7),
                nova("Comprar presente de aniversário", "",
                        "OUTROS",   "MEDIA", "EM_ANDAMENTO", 1),
                nova("Renovar carteirinha da biblioteca","",
                        "OUTROS",   "BAIXA", "PENDENTE",     4)
        ));
    }

    // O minusDays espalha as datas de criação pra lista não ficar
    // toda com o mesmo horário.
    private static Document nova(String titulo, String descricao,
                                 String categoria, String prioridade,
                                 String status, int diasAtras){
        return new Document()
                .append("titulo",      titulo)
                .append("descricao",   descricao)
                .append("categoria",   categoria)
                .append("prioridade",  prioridade)
                .append("status",      status)
                .append("dataCriacao", LocalDateTime.now().minusDays(diasAtras).toString());
    }

    // Construtor privado pra ninguém instanciar isso por engano.
    private MongoConfig(){}
}
