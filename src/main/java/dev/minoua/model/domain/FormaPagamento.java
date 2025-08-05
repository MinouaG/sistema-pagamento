package dev.minoua.model.domain;

public abstract class FormaPagamento {
    private int id;
    private boolean dadosValidos = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDadosValidos() {
        return dadosValidos;
    }


    public static boolean validarDados(){return false;}
    public static String retornarResultado(boolean resultadoValidacao){return null;}
}
