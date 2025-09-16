package dev.minoua.model;

import lombok.Getter;
import lombok.Setter;

public abstract class FormaPagamentoModel extends Model<FormaPagamentoModel> {
    public FormaPagamentoModel(String fileName) {
        super(fileName);
    }
    public abstract String getTipo();
    public abstract boolean validarDados();
}
