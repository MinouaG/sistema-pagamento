package dev.minoua;

import dev.minoua.model.domain.*;

public class Main {
    final static String arquivoEntrada = "solicitacoes.csv";
    final static String arquivoSaida = "solicitacoesNovas.csv";

    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();

        SolicitacaoPagamento solicitacao = new SolicitacaoPagamento(6, 1, new PixPagamento("mariofabio@mail.com"),"Processando", 99.99);

        try{
            arquivo.lerArquivoCsv(arquivoEntrada);
            arquivo.escreverArquivoCsv(arquivoSaida, solicitacao);
        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
        }
    }
}