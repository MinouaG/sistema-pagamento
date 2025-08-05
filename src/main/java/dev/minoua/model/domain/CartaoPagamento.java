package dev.minoua.model.domain;

import java.util.Date;

public class CartaoPagamento extends FormaPagamento{
    private String numero;
    private String nomeTitular;
    private String cpfTitular;
    private Date validade;
    private int cvv;
}
