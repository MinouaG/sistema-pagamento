package dev.minoua;

import dev.minoua.model.Arquivo;
import dev.minoua.model.PixPagamentoModel;
import dev.minoua.model.SolicitacaoPagamentoModel;

public class Main {
    final static String arquivoEntrada = "solicitacoes.csv";
    final static String arquivoSaida = "solicitacoesNovas.csv";

    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();

        SolicitacaoPagamentoModel solicitacao = new SolicitacaoPagamentoModel(6L, 1L, new PixPagamentoModel("mariofabio@mail.com"),"Processando", 99.99f);

        try{
            arquivo.lerArquivoCsv(arquivoEntrada);
            arquivo.escreverArquivoCsv(arquivoSaida, solicitacao);
        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
        }
    }
}