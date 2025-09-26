package dev.minoua.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PixPagamentoModel extends FormaPagamentoModel {
    private String codigo;

    public PixPagamentoModel() {
        super("pix_pagamento.csv");
    }


    @Override
    public PixPagamentoModel fromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            PixPagamentoModel obj = new PixPagamentoModel();
            obj.setId(Long.parseLong(values[0]));
            obj.setCreatedAt(values[1].equals("null") ? null : java.time.LocalDateTime.parse(values[1], formatter));
            obj.setUpdatedAt(values[2].equals("null") ? null : java.time.LocalDateTime.parse(values[2], formatter));
            obj.setCodigo(values[3]);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validarDados() {
        return codigo != null && !codigo.isEmpty();
    }

    @Override
    public List<String> getCsvFieldOrder() {
        return List.of("id", "createdAt", "updatedAt", "codigo");
    }

    @Override
    public String toCSV() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",",
            String.valueOf(getId()),
            getCreatedAt() == null ? "null" : getCreatedAt().format(formatter),
            getUpdatedAt() == null ? "null" : getUpdatedAt().format(formatter),
            codigo == null ? "" : codigo
        );
    }

}

