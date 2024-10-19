package br.com.Julimar.TabelaFipe.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    //devolve uma String com o json que corresponde a resposta da requisição
    public String obterDados(String url){
        //criação do cliente http
        HttpClient client = HttpClient.newHttpClient();
        //construção da requisição htttp com o endereço fornecido
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        //variavel  que ira armazenar a resposta da requisição
        HttpResponse<String> response = null;
        try{
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString()); //converte o corpo da resposta em String
        }catch(IOException e){
            throw new RuntimeException(e);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        //devolve uma String com o json que corresponde a resposta da requisição
        String json = response.body();
        return json;
    }

}
