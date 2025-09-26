package dev.minoua.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CartaoPagamentoModel extends FormaPagamentoModel {
    private String numero;
    private String nomeTitular;
    private String cpfTitular;
    private Date validade;
    private int cvv;

    public CartaoPagamentoModel() {
        super("cartoes_pagamento.csv");
    }


    @Override
    public CartaoPagamentoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            CartaoPagamentoModel obj = new CartaoPagamentoModel();
            obj.setId(Long.parseLong(values[0]));
            obj.setCreatedAt(values[1].equals("null") ? null : java.time.LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt(values[2].equals("null") ? null : java.time.LocalDateTime.parse(values[2], formatter));
            obj.setNumero(values[3]);
            obj.setNomeTitular(values[4]);
            obj.setCpfTitular(values[5]);
            obj.setValidade(values[6].equals("null") ? null : sdf.parse(values[6]));
            obj.setCvv(Integer.parseInt(values[7]));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validarDados() {
        return numero != null && !numero.isEmpty()
            && nomeTitular != null && !nomeTitular.isEmpty()
            && cpfTitular != null && !cpfTitular.isEmpty()
            && validade != null
            && cvv > 0;
    }

    @Override
    public List<String> getCsvFieldOrder() {
        return List.of("id", "createdAt", "updatedAt", "numero", "nomeTitular", "cpfTitular", "validade", "cvv");
    }

    @Override
    public String toCSV() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return String.join(",",
            String.valueOf(getId()),
            getCreatedAt() == null ? "null" : getCreatedAt().format(formatter),
            getUpdatedAt() == null ? "null" : getUpdatedAt().format(formatter),
            numero == null ? "" : numero,
            nomeTitular == null ? "" : nomeTitular,
            cpfTitular == null ? "" : cpfTitular,
            validade == null ? "null" : sdf.format(validade),
            String.valueOf(cvv)
        );
    }
}
