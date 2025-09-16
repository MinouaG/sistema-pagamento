package dev.minoua;

import dev.minoua.controller.TransacaoController;
import dev.minoua.model.TransacaoModel;
import io.javalin.Javalin;
import dev.minoua.controller.SolicitacaoPagamentoController;
import dev.minoua.model.SolicitacaoPagamentoModel;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) throws Exception {
        SolicitacaoPagamentoController solicitacaoController = new SolicitacaoPagamentoController();
        TransacaoController transacaoController = new TransacaoController();

        var app = Javalin.create()
                .get("/solicitacaopagamento", ctx -> ctx.json(solicitacaoController.get()))
                .get("/solicitacaopagamento/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    SolicitacaoPagamentoModel solicitacao = (SolicitacaoPagamentoModel) solicitacaoController.get(id);
                    if (solicitacao != null) {
                        ctx.json(solicitacao);
                    } else {
                        ctx.status(404).result("Solicitação de pagamento não encontrada");
                    }
                })
                .post("/solicitacaopagamento/", ctx -> {
                    SolicitacaoPagamentoModel novaSolicitacao = ctx.bodyAsClass(SolicitacaoPagamentoModel.class);
                    solicitacaoController.post(novaSolicitacao);
                    ctx.status(201).json(novaSolicitacao);
                })
                .put("/solicitacaopagamento/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    SolicitacaoPagamentoModel atualizacao = ctx.bodyAsClass(SolicitacaoPagamentoModel.class);
                    SolicitacaoPagamentoModel solicitacaoAtualizada = solicitacaoController.put(id, atualizacao);
                    if (solicitacaoAtualizada != null) {
                        ctx.json(solicitacaoAtualizada);
                    } else {
                        ctx.status(404).result("Solicitação de pagamento não encontrada");
                    }
                })
                .patch("/solicitacaopagamento/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    SolicitacaoPagamentoModel atualizacao = ctx.bodyAsClass(SolicitacaoPagamentoModel.class);
                    SolicitacaoPagamentoModel solicitacaoAtualizada = solicitacaoController.patch(id, atualizacao);
                    if (solicitacaoAtualizada != null) {
                        ctx.json(solicitacaoAtualizada);
                    } else {
                        ctx.status(404).result("Solicitação de pagamento não encontrada");
                    }
                })
                .delete("/solicitacaopagamento/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    boolean deleted = solicitacaoController.delete(id);
                    if (deleted) {
                        ctx.status(204);
                    } else {
                        ctx.status(404).result("Solicitação de pagamento não encontrada");
                    }
                })
                .get("/transacao", ctx -> ctx.json(transacaoController.get()))
                .get("/transacao/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    TransacaoModel transacao = (TransacaoModel) transacaoController.get(id);
                    if (transacao != null) {
                        ctx.json(transacao);
                    } else {
                        ctx.status(404).result("Transacao não encontrada");
                    }
                })
                .post("/transacao/", ctx -> {
                    TransacaoModel novaTransacao = ctx.bodyAsClass(TransacaoModel.class);
                    transacaoController.post(novaTransacao);
                    ctx.status(201).json(novaTransacao);
                })
                .put("/transacao/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    TransacaoModel atualizacao = ctx.bodyAsClass(TransacaoModel.class);
                    TransacaoModel transacaoAtualizada = transacaoController.put(id, atualizacao);
                    if (transacaoAtualizada != null) {
                        ctx.json(transacaoAtualizada);
                    } else {
                        ctx.status(404).result("Transacao não encontrada");
                    }
                })
                .patch("/transacao/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    TransacaoModel atualizacao = ctx.bodyAsClass(TransacaoModel.class);
                    TransacaoModel transacaoAtualizada = transacaoController.patch(id, atualizacao);
                    if (transacaoAtualizada != null) {
                        ctx.json(transacaoAtualizada);
                    } else {
                        ctx.status(404).result("Transacao não encontrada");
                    }
                })
                .delete("/transacao/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    boolean deleted = transacaoController.delete(id);
                    if (deleted) {
                        ctx.status(204);
                    } else {
                        ctx.status(404).result("Transacao não encontrada");
                    }
                })
                .start(7000);
    }
}