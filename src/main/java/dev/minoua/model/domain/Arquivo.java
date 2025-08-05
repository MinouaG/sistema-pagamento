package dev.minoua.model.domain;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class Arquivo {

    public void lerArquivoCsv(String nomeArquivo) throws Exception {
        try {
            InputStream inputStream = Arquivo.class.getClassLoader().getResourceAsStream(nomeArquivo);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> linhas = reader.readAll();

            for (String[] linha : linhas) {
                // Printando todos os campos do arquivo de entrada
                for (String campo : linha) {
                    System.out.printf(campo + " | ");
                }
                System.out.println();
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void escreverArquivoCsv(String nomeArquivo, SolicitacaoPagamento solicitacaoPagamento) throws Exception {
        try {
            File arquivo = new File(nomeArquivo);
            boolean novoArquivo = !arquivo.exists() || arquivo.length() == 0;

            CSVWriter writer = new CSVWriter(new FileWriter(nomeArquivo, true), CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );
            if (novoArquivo) {
                writer.writeNext(solicitacaoPagamento.criarCabecalhoCsv());
            }
            writer.writeNext(solicitacaoPagamento.criarLinhaCsv());
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}



