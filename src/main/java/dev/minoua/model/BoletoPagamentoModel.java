package dev.minoua.model;

import java.util.Date;

public class BoletoPagamentoModel extends FormaPagamentoModel {
    private String codigo;
    private String nome;
    private Date dataVencimento;
    private String cpf;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BoletoPagamentoModel(String codigo, String nome, Date dataVencimento, String cpf) {
        this.codigo = codigo;
        this.nome = nome;
        this.dataVencimento = dataVencimento;
        this.cpf = cpf;
    }

    public String toString(){
        return "Boleto"+codigo;
    }
}
