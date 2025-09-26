package dev.minoua.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class SolicitacaoPagamentoModel extends Model<SolicitacaoPagamentoModel> {
    private Long idPedido;
    private StatusPagamento status;
    private Float valorTotal;

    @JsonIgnore
    private FormaPagamentoModel formaPagamentoModel;

    public SolicitacaoPagamentoModel() {
        super("solicitacoes_pagamento.csv");
    }

    // Regras de negócio
    public void atualizarStatus(StatusPagamento novoStatus) {
        this.setStatus(novoStatus);
        this.setUpdatedAt(LocalDateTime.now());
        this.update(this.getId(), this);
    }

    public void atualizarStatusPorRespostaTransacao(String respostaTransacao) {
        switch (respostaTransacao.toUpperCase()) {
            case "APROVADO" -> this.setStatus(StatusPagamento.APROVADO);
            case "RECUSADO" -> this.setStatus(StatusPagamento.RECUSADO);
            case "PENDENTE" -> this.setStatus(StatusPagamento.PENDENTE);
            default -> this.setStatus(StatusPagamento.FORMA_PAGAMENTO_INVALIDA);
        }
        this.setUpdatedAt(LocalDateTime.now());
        this.update(this.getId(), this);
    }

    public TransacaoModel gerarTransacao() {
        List<TransacaoModel> transacoes = new TransacaoModel().list();
        long novoId = transacoes.stream()
                .mapToLong(t -> t.getId() != null ? t.getId() : 0)
                .max().orElse(0) + 1;

        TransacaoModel transacao = new TransacaoModel();
        transacao.setId(novoId);
        transacao.setIdSolicitacaoPagamento(this.getId());
        transacao.setValorTotal(this.valorTotal);
        transacao.setFormaPagamento(this.formaPagamentoModel);

        transacao.save(transacao);
        this.setStatus(StatusPagamento.PROCESSANDO);

        return transacao;
    }

    public TransacaoModel validarDadosPagamento() {
        if (this.formaPagamentoModel != null && this.formaPagamentoModel.validarDados()) {
            return this.gerarTransacao();
        }
        this.atualizarStatus(StatusPagamento.FORMA_PAGAMENTO_INVALIDA);
        return null;
    }

    public StatusPagamento getStatus() {
        return status != null ? status : StatusPagamento.PENDENTE;
    }

    // CSV
    @Override
    public SolicitacaoPagamentoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            SolicitacaoPagamentoModel obj = new SolicitacaoPagamentoModel();
            obj.setId(Long.parseLong(values[0]));
            obj.setCreatedAt("null".equals(values[1]) ? null : LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt("null".equals(values[2]) ? obj.getCreatedAt() : LocalDateTime.parse(values[2], formatter));
            obj.setIdPedido("null".equals(values[3]) ? null : Long.parseLong(values[3]));
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
                getIdPedido() == null ? "null" : String.valueOf(getIdPedido()),
                status.name(),
                String.valueOf(valorTotal)
        );
    }

    @Override
    public List<String> getCsvFieldOrder() {
        return List.of("id","createdAt","updatedAt","idPedido","status","valorTotal");
    }
}