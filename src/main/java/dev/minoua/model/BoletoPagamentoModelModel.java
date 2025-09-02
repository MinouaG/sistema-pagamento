package dev.minoua.model;

import java.util.Date;

public class BoletoPagamento extends FormaPagamento{
    private String codigo;
    private Date dataVencimento;

    public BoletoPagamento(String codigo, Date dataVencimento) {
        this.codigo = codigo;
        this.dataVencimento = dataVencimento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String toString(){
        return "Boleto"+codigo;
    }
}
