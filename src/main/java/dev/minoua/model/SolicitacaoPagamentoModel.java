package dev.minoua.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolicitacaoPagamentoModel extends Model {
    private Long idPedido;
    private FormaPagamentoModel formaPagamentoModel;
    private String status = "Pendente";
    private float valorTotal;

    // Construtor
    public SolicitacaoPagamentoModel(Long id, Long idPedido, FormaPagamentoModel formaPagamentoModel, String statusPagamento, float valorTotal) {
        this.setId(id);
        this.idPedido = idPedido;
        this.formaPagamentoModel = formaPagamentoModel;
        this.status = statusPagamento;
        this.valorTotal = valorTotal;
        this.setCreatedAt(LocalDateTime.now());
    }

    // Getters e Setters

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public FormaPagamentoModel getFormaPagamento() { return formaPagamentoModel; }
    public void setFormaPagamento(FormaPagamentoModel formaPagamentoModel) { this.formaPagamentoModel = formaPagamentoModel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(float valorTotal) { this.valorTotal = valorTotal; }

    // Metodos de negocio
    public void atualizarStatus(String novoStatus) {
        this.status = novoStatus;
    }

    public TransacaoModel gerarTransacao(Long idTransacao) {
        return new TransacaoModel(idTransacao, this.getId(), this.valorTotal);
    }

    public boolean validarTransacao(TransacaoModel transacao) {
        if (transacao.validarTransacao()){
            this.atualizarStatus("Aprovado");
            return true;
        }
        this.atualizarStatus("Recusado");
        return false;
    }

    public TransacaoModel validarDadosPagamento(Long idTransacao){
        if (this.formaPagamentoModel.validarDados()) {
            return this.gerarTransacao(idTransacao);
        }
        this.atualizarStatus("Forma de pagamento invalida.");
        return null;
    }


    // Metodos para manipulacao de arquivos com OpenCsv
    public String[] criarCabecalhoCsv() {
        return new String[] {
                "id",
                "idPedido",
                "formaPagamento",
                "statusPagamento",
                "valor",
                "dataSolicitacao"
        };
    }

    public String[] criarLinhaCsv() {
        return new String[] {
                String.valueOf(this.getId()),
                String.valueOf(idPedido),
                formaPagamentoModel.toString(),
                status,
                String.valueOf(valorTotal),
                this.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        };
    }
}
