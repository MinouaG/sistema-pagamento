package dev.minoua.model.domain;


import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolicitacaoPagamento {
    private int id;
    private int idPedido;
    private FormaPagamento formaPagamento;
    private String statusPagamento = "Pendente";
    private double valor;
    private LocalDateTime dataSolicitacao;

    // Construtor
    public SolicitacaoPagamento(int id, int idPedido, FormaPagamento formaPagamento, String statusPagamento, double valor) {
        this.id = id;
        this.idPedido = idPedido;
        this.formaPagamento = formaPagamento;
        this.statusPagamento = statusPagamento;
        this.valor = valor;
        this.dataSolicitacao = LocalDateTime.now();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }


    // Metodos de negocio
    public void atualizarStatus(String novoStatus) {
        this.statusPagamento = novoStatus;
    }

    public Transacao registrarTransacao(int idTransacao) {
        return new Transacao(idTransacao, this.id, this.valor);
    }

    public boolean validarTransacao(Transacao transacao) {
        if (transacao.validarTransacao()){
            this.atualizarStatus("Aprovado");
            return true;
        }
        this.atualizarStatus("Recusado");
        return false;
    }

    public Transacao validarDados(int idTransacao){
        if (this.formaPagamento.isDadosValidos()) {
            return this.registrarTransacao(idTransacao);
        }
        this.atualizarStatus("Forma de pagamento invalida.");
        return null;
    }


    // Metodos para manipulacao de arquivos com OpenCsv
    public static String[] criarCabecalhoCsv() {
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
                String.valueOf(id),
                String.valueOf(idPedido),
                formaPagamento.toString(),
                statusPagamento,
                String.valueOf(valor),
                dataSolicitacao.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        };
    }
}
