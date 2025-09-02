package dev.minoua.model;

import java.time.LocalDateTime;

public class TransacaoModel extends Model {
    private Long id;
    private LocalDateTime dataHora;
    private Long idSolicitacaoPagamento;
    private String status = "Processando";
    private double valor;

    public TransacaoModel(Long id, LocalDateTime dataHora, Long idSolicitacaoPagamento, String status, double valor) {
        this.setId(id);
        this.dataHora = dataHora;
        this.idSolicitacaoPagamento = idSolicitacaoPagamento;
        this.status = status;
        this.valor = valor;
    }

    public TransacaoModel(Long id, Long idSolicitacaoPagamento, double valor) {
        this.setId(id);
        this.idSolicitacaoPagamento = idSolicitacaoPagamento;
        this.valor = valor;
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


