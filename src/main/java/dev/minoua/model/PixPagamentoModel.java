package dev.minoua.model;

public class PixPagamentoModel extends FormaPagamentoModel {
    private String codigo;

    public PixPagamentoModel(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return "Pix:" + codigo;
    }

}
