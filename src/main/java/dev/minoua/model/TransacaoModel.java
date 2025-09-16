package dev.minoua.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class TransacaoModel extends Model<TransacaoModel> {
    @Getter
    @Setter
    private Long idSolicitacaoPagamento;
    @Getter
    @Setter
    private StatusPagamento status = StatusPagamento.PENDENTE;
    @Getter
    @Setter
    private float valorTotal;
    @Getter
    @Setter
    @JsonIgnore
    private FormaPagamentoModel formaPagamento;
    @Getter
    @Setter
    private String tipoPagamento; // campo auxiliar para serialização


    public TransacaoModel() {
        super("transacoes.csv");
    }

    public void iniciarPagamento(){

    }

    public void processarResultadoTransacao(String respostaApi) {
        if ("APROVADO".equalsIgnoreCase(respostaApi)) {
            this.setStatus(StatusPagamento.APROVADO);
        } else if ("RECUSADO".equalsIgnoreCase(respostaApi)) {
            this.setStatus(StatusPagamento.RECUSADO);
        } else if ("PENDENTE".equalsIgnoreCase(respostaApi)) {
            this.setStatus(StatusPagamento.PENDENTE);
        } else {
            this.setStatus(StatusPagamento.FORMA_PAGAMENTO_INVALIDA);
        }
        this.setUpdatedAt(LocalDateTime.now());
        this.update(this.getId(), this);
    }

    @Override
    public TransacaoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            TransacaoModel obj = new TransacaoModel();
            obj.setId(Long.parseLong(values[0]));
            obj.setCreatedAt(values[1].equals("null") ? null : java.time.LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt(values[2].equals("null") ? null : java.time.LocalDateTime.parse(values[2], formatter));
            obj.setIdSolicitacaoPagamento(Long.parseLong(values[3]));
            obj.setStatus(StatusPagamento.valueOf(values[4]));
            obj.setValorTotal(Float.parseFloat(values[5]));
            // Define o tipo da forma de pagamento, se existir
            if (values.length > 6) {
                String tipo = values[6];
                if ("cartao".equalsIgnoreCase(tipo)) {
                    obj.setFormaPagamento(new CartaoPagamentoModel());
                } else if ("pix".equalsIgnoreCase(tipo)) {
                    obj.setFormaPagamento(new PixPagamentoModel());
                } // pode adicionar outros tipos futuramente
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

    public void transformarFormaTipoPagamento() {
        if (formaPagamento instanceof CartaoPagamentoModel) setTipoPagamento("CARTAO");
        if (formaPagamento instanceof PixPagamentoModel) setTipoPagamento("PIX");
        if (formaPagamento instanceof BoletoPagamentoModel) setTipoPagamento("BOLETO");
    }

    @Override
    public String toCSV() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",",
            String.valueOf(getId()),
            getCreatedAt() == null ? "null" : getCreatedAt().format(formatter),
            getUpdatedAt() == null ? "null" : getUpdatedAt().format(formatter),
            String.valueOf(getIdSolicitacaoPagamento()),
            getStatus().name(),
            String.valueOf(getValorTotal()),
            getTipoPagamento()
        );
    }

}
