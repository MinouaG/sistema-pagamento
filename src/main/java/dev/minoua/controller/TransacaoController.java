package dev.minoua.controller;

import dev.minoua.model.SolicitacaoPagamentoModel;
import dev.minoua.model.TransacaoModel;
import io.javalin.Javalin;

import java.util.List;

public class TransacaoController extends Controller<TransacaoModel> {
    private final TransacaoModel model = new TransacaoModel();

    public static void config(Javalin app) {
        TransacaoController controller = new TransacaoController();

        app.get("/transacao", ctx -> ctx.json(controller.getAll()));

        app.get("/transacao/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            TransacaoModel transacao = (TransacaoModel) controller.getById(id);
            if (transacao != null) {
                ctx.json(transacao);
            } else {
                ctx.status(404).result("Transacao não encontrada");
            }
        });

        app.post("/transacao", ctx -> {
            TransacaoModel novaTransacao = ctx.bodyAsClass(TransacaoModel.class);
            controller.create(novaTransacao);
            ctx.status(201).json(novaTransacao);
        });
        app.put("/transacao/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            TransacaoModel atualizacao = ctx.bodyAsClass(TransacaoModel.class);
            TransacaoModel transacaoAtualizada = controller.update(id, atualizacao);
            if (transacaoAtualizada != null) {
                ctx.json(transacaoAtualizada);
            } else {
                ctx.status(404).result("Transacao não encontrada");
            }
        });
        app.patch("/transacao/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            TransacaoModel atualizacao = ctx.bodyAsClass(TransacaoModel.class);
            TransacaoModel transacaoAtualizada = controller.partialUpdate(id, atualizacao);
            if (transacaoAtualizada != null) {
                ctx.json(transacaoAtualizada);
            } else {
                ctx.status(404).result("Transacao não encontrada");
            }
        });
        app.delete("/transacao/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            boolean deleted = controller.delete(id);
            if (deleted) {
                ctx.status(204);
            } else {
                ctx.status(404).result("Transacao não encontrada");
            }
        });
    }
    @Override
    public List<TransacaoModel> getAll() {
        return model.list();
    }

    @Override
    public TransacaoModel getById(Long id) {
        return model.find(id);
    }

    @Override
    public TransacaoModel create(TransacaoModel object) {
        model.save(object);
        return object;
    }

    @Override
    public TransacaoModel update(Long id, TransacaoModel object) {
        return model.update(id, object) ? object : null;
    }

    @Override
    public TransacaoModel partialUpdate(Long id, TransacaoModel object) {
        return model.partialUpdate(id, object) ? model.find(id) : null;
    }

    @Override
    public boolean delete(Long id) {
        return model.delete(id);
    }
}
