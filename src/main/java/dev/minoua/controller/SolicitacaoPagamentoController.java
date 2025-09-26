package dev.minoua.controller;

import dev.minoua.model.SolicitacaoPagamentoModel;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SolicitacaoPagamentoController extends Controller<SolicitacaoPagamentoModel> {

    private final SolicitacaoPagamentoModel model = new SolicitacaoPagamentoModel();

    public static void config(Javalin app) {
        SolicitacaoPagamentoController controller = new SolicitacaoPagamentoController();

        app.get("/solicitacaopagamento", ctx -> {
            List<SolicitacaoPagamentoModel> lista = controller.getAll();
            ctx.status(200).json(lista);
        });

        app.get("/solicitacaopagamento/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            SolicitacaoPagamentoModel solicitacao = controller.getById(id);
            if (solicitacao != null) {
                ctx.status(200).json(solicitacao);
            } else {
                ctx.status(404).result("Solicitação de pagamento não encontrada: " + id);
            }
        });

        app.post("/solicitacaopagamento", ctx -> {
            SolicitacaoPagamentoModel novaSolicitacao = ctx.bodyAsClass(SolicitacaoPagamentoModel.class);
            controller.create(novaSolicitacao);
            ctx.status(201).json(novaSolicitacao);
        });

        app.put("/solicitacaopagamento/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            SolicitacaoPagamentoModel atualizacao = ctx.bodyAsClass(SolicitacaoPagamentoModel.class);
            SolicitacaoPagamentoModel atualizada = controller.update(id, atualizacao);
            if (atualizada != null) {
                ctx.json(atualizada);
            } else {
                ctx.status(404).result("Solicitação de pagamento não encontrada");
            }
        });

        app.patch("/solicitacaopagamento/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            SolicitacaoPagamentoModel atualizacao = ctx.bodyAsClass(SolicitacaoPagamentoModel.class);
            SolicitacaoPagamentoModel atualizada = controller.partialUpdate(id, atualizacao);
            if (atualizada != null) {
                ctx.json(atualizada);
            } else {
                ctx.status(404).result("Solicitação de pagamento não encontrada");
            }
        });

        app.delete("/solicitacaopagamento/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            boolean deleted = controller.delete(id);
            if (deleted) {
                ctx.status(204);
            } else {
                ctx.status(404).result("Solicitação de pagamento não encontrada");
            }
        });
    }

    public List<SolicitacaoPagamentoModel> getAll() {
        return model.list();
    }

    public SolicitacaoPagamentoModel getById(Long id) {
        return model.find(id);
    }

    public SolicitacaoPagamentoModel create(SolicitacaoPagamentoModel object) {
        model.save(object);
        return object;
    }

    @Override
    public SolicitacaoPagamentoModel update(Long id, SolicitacaoPagamentoModel object) {
        return model.update(id, object) ? object : null;
    }

    @Override
    public SolicitacaoPagamentoModel partialUpdate(Long id, SolicitacaoPagamentoModel object) {
        boolean updated = model.partialUpdate(id, object);
        return updated ? model.find(id) : null;
    }

    public boolean delete(Long id) {
        return model.delete(id);
    }
}
