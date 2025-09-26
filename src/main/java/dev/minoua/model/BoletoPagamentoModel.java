package dev.minoua.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BoletoPagamentoModel extends FormaPagamentoModel {
    private String codigo;
    private String nome;
    private Date dataVencimento;
    private String cpf;

   public BoletoPagamentoModel() {
        super("boletos_pagamento.csv");
    }



    @Override
    public List<String> getCsvFieldOrder() {
        return List.of("id", "createdAt", "updatedAt", "codigo", "nome", "dataVencimento", "cpf");
    }

    @Override
    public BoletoPagamentoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            BoletoPagamentoModel obj = new BoletoPagamentoModel();
            obj.setId(Long.parseLong(values[0]));
            obj.setCreatedAt(values[1].equals("null") ? null : java.time.LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt(values[2].equals("null") ? null : java.time.LocalDateTime.parse(values[2], formatter));
            obj.setCodigo(values[3]);
            obj.setNome(values[4]);
            obj.setDataVencimento(values[5].equals("null") ? null : sdf.parse(values[5]));
            obj.setCpf(values[6]);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validarDados() {
        return true;
    }
}
