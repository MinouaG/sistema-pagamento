package dev.minoua.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class TransacaoModel extends Model<TransacaoModel> {
    private Long idSolicitacaoPagamento;
    private StatusPagamento status;
    private Float valorTotal;

    @JsonIgnore
    private FormaPagamentoModel formaPagamento;
    private String tipoPagamento;

    // CONSTRUTOR
    public TransacaoModel() {
        super("transacoes.csv");
    }

    public void iniciarPagamento() {
    }

    // REGRAS DE NEGÓCIO
    public void processarResultadoTransacao(String respostaApi) {
        switch (respostaApi.toUpperCase()) {
            case "APROVADO" -> this.setStatus(StatusPagamento.APROVADO);
            case "RECUSADO" -> this.setStatus(StatusPagamento.RECUSADO);
            case "PENDENTE" -> this.setStatus(StatusPagamento.PENDENTE);
            default -> this.setStatus(StatusPagamento.FORMA_PAGAMENTO_INVALIDA);
        }
        this.setUpdatedAt(LocalDateTime.now());
        this.update(this.getId(), this);
    }

    public StatusPagamento getStatus() {
        return status != null ? status : StatusPagamento.PENDENTE;
    }

    // METODOS DE MANIPULACAO CSV
    @Override
    public TransacaoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            TransacaoModel obj = new TransacaoModel();
            obj.setId(Long.parseLong(values[0]));
            obj.setCreatedAt("null".equals(values[1]) ? null : LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt("null".equals(values[2]) ? obj.getCreatedAt() : LocalDateTime.parse(values[2], formatter));
            obj.setIdSolicitacaoPagamento(Long.parseLong(values[3]));
            obj.setStatus(StatusPagamento.valueOf(values[4]));
            obj.setValorTotal(Float.parseFloat(values[5]));

            if (values.length > 6) {
                obj.setTipoPagamento(values[6].toUpperCase());
                obj.setFormaPagamento(criarFormaPagamentoPorTipo(obj.getTipoPagamento()));
            }

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<String> getCsvFieldOrder() {
        return List.of("id", "createdAt", "updatedAt", "idSolicitacaoPagamento", "status", "valorTotal", "tipoPagamento");
    }

    @Override
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",",
                String.valueOf(getId()),
                getCreatedAt() == null ? "null" : getCreatedAt().format(formatter),
                getUpdatedAt() == null ? "null" : getUpdatedAt().format(formatter),
                String.valueOf(getIdSolicitacaoPagamento()),
                status.name(),
                String.valueOf(valorTotal),
                tipoPagamento != null ? tipoPagamento : ""
        );
    }


    // METODOS AUXILIARES

    private FormaPagamentoModel criarFormaPagamentoPorTipo(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "CARTAO" -> new CartaoPagamentoModel();
            case "PIX" -> new PixPagamentoModel();
            case "BOLETO" -> new BoletoPagamentoModel();
            default -> null;
        };
    }

    public void transformarFormaTipoPagamento() {
        if (formaPagamento instanceof CartaoPagamentoModel) setTipoPagamento("CARTAO");
        if (formaPagamento instanceof PixPagamentoModel) setTipoPagamento("PIX");
        if (formaPagamento instanceof BoletoPagamentoModel) setTipoPagamento("BOLETO");
    }
}