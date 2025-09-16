package dev.minoua.controller;

import dev.minoua.model.BoletoPagamentoModel;
import dev.minoua.model.FormaPagamentoModel;

public class BoletoPagamentoController extends Controller<FormaPagamentoModel> {
    public BoletoPagamentoController(){
        this.model = new BoletoPagamentoModel();
    }
}
