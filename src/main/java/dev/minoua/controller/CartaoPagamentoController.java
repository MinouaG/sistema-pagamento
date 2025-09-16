package dev.minoua.controller;

import dev.minoua.model.CartaoPagamentoModel;
import dev.minoua.model.FormaPagamentoModel;

public class CartaoPagamentoController extends Controller<FormaPagamentoModel>{
    public CartaoPagamentoController(){
        this.model = new CartaoPagamentoModel();
    }
}
