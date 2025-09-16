package dev.minoua.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

@Getter
@Setter
public class SolicitacaoPagamentoModel extends Model<SolicitacaoPagamentoModel> {
    private Long idPedido;
    private StatusPagamento status = StatusPagamento.PENDENTE;
    private float valorTotal;
    @JsonIgnore
    private FormaPagamentoModel formaPagamentoModel;

    // Construtor
    public SolicitacaoPagamentoModel() {
        super("solicitacoes_pagamento.csv");
    }

    // Metodos de regras de negocio
    public void atualizarStatus(StatusPagamento novoStatus) {
        this.status = novoStatus;
        this.setUpdatedAt(LocalDateTime.now());
        this.update(this.getId(), this);
    }

    public void atualizarStatusPorRespostaTransacao(String respostaTransacao) {
        if ("APROVADO".equalsIgnoreCase(respostaTransacao)) {
            this.setStatus(StatusPagamento.APROVADO);
        } else if ("RECUSADO".equalsIgnoreCase(respostaTransacao)) {
            this.setStatus(StatusPagamento.RECUSADO);
        } else if ("PENDENTE".equalsIgnoreCase(respostaTransacao)) {
            this.setStatus(StatusPagamento.PENDENTE);
        } else {
            this.setStatus(StatusPagamento.FORMA_PAGAMENTO_INVALIDA);
        }
        this.setUpdatedAt(LocalDateTime.now());
        this.update(this.getId(), this);
    }

    public TransacaoModel gerarTransacao() {
        List<TransacaoModel> transacoes = new TransacaoModel().list();
        long maxId = 0L;
        for (TransacaoModel t : transacoes) {
            if (t.getId() != null && t.getId() > maxId) {
                maxId = t.getId();
            }
        }
        long novoId = maxId + 1;
        TransacaoModel transacao = new TransacaoModel();
        transacao.setId(novoId);
        transacao.setIdSolicitacaoPagamento(this.getId());
        transacao.setValorTotal(this.valorTotal);
        transacao.setFormaPagamento(this.formaPagamentoModel);
        transacao.save(transacao);
        this.formaPagamentoModel.save(formaPagamentoModel);
        this.setStatus(StatusPagamento.PROCESSANDO);
        transacao.setFormaPagamento(this.formaPagamentoModel);

        return transacao;
    }


    public TransacaoModel validarDadosPagamento() {
        if (this.formaPagamentoModel != null && this.formaPagamentoModel.validarDados()) {
            return this.gerarTransacao();
        }
        this.atualizarStatus(StatusPagamento.FORMA_PAGAMENTO_INVALIDA);
        return null;
    }

    @Override
    public SolicitacaoPagamentoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            SolicitacaoPagamentoModel obj = new SolicitacaoPagamentoModel();

            obj.setId(parseLong(values[0]));
            obj.setCreatedAt(values[1].equals("null") ? null : LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt(values[2].equals("null") ? null : LocalDateTime.parse(values[2], formatter));
            obj.setIdPedido("null".equals(values[3]) ? null : parseLong(values[3]));
            obj.setStatus(StatusPagamento.valueOf(values[4]));
            obj.setValorTotal(Float.parseFloat(values[5]));

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",",
            String.valueOf(getId()),
            getCreatedAt() == null ? "null" : getCreatedAt().format(formatter),
            getUpdatedAt() == null ? "null" : getUpdatedAt().format(formatter),
            String.valueOf(getIdPedido()),
            status.name(),
            String.valueOf(getValorTotal())
        );
    }


    @Override
    public List<String> getCsvFieldOrder() {
        return List.of("id","createdAt","updatedAt","idPedido","status","valorTotal");
    }
}
