package dev.minoua.model;

public class PixPagamento extends FormaPagamento{
    private String chave;

    public PixPagamento(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }

    @Override
    public String toString() {
        return "Pix:" + chave;
    }

}
