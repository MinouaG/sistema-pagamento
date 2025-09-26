package dev.minoua;

import dev.minoua.controller.TransacaoController;
import dev.minoua.model.TransacaoModel;
import io.javalin.Javalin;
import dev.minoua.controller.SolicitacaoPagamentoController;
import dev.minoua.model.SolicitacaoPagamentoModel;

public class Main {
    public static void main(String[] args) throws Exception {
        var app = Javalin.create();

        // Configura os controladores
        SolicitacaoPagamentoController.config(app);
        TransacaoController.config(app);

        app.start(7000);
    }
}