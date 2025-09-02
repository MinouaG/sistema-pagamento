package dev.minoua.model;

import java.time.LocalDateTime;

public class Transacao {
    private int id;
    private LocalDateTime dataHora;
    private int idSolicitacaoPagamento;
    private String status = "Processando";
    private double valor;

    public Transacao(int id, LocalDateTime dataHora, int idSolicitacaoPagamento, String status, double valor) {
        this.id = id;
        this.dataHora = dataHora;
        this.idSolicitacaoPagamento = idSolicitacaoPagamento;
        this.status = status;
        this.valor = valor;
    }

    public Transacao(Long id, Long idSolicitacaoPagamento, double valor) {
        this.id = id;
        this.idSolicitacaoPagamento = idSolicitacaoPagamento;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean validarTransacao(){
        return true;
    }

}


