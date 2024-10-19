package br.com.Julimar.TabelaFipe.principal;

import br.com.Julimar.TabelaFipe.model.Dados;
import br.com.Julimar.TabelaFipe.model.Modelos;
import br.com.Julimar.TabelaFipe.model.Veiculo;
import br.com.Julimar.TabelaFipe.service.ConsumoApi;
import br.com.Julimar.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scan = new Scanner(System.in);
    private final String url_base = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void menu(){
        String menu = """
                ******* ESCOLHA UMA OPÇÃO PARA CONSULTA *******
                ******************* Carro *********************
                ******************** Moto *********************
                ****************** Caminhão *******************
                ***********************************************
                """;
        System.out.println(menu);
        String opcao = scan.nextLine();
        String url;

        if(opcao.toLowerCase().contains("carr")){
            url = url_base + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")){
            url = url_base + "motos/marcas";
        } else if (opcao.toLowerCase().contains("caminh")){
            url = url_base + "caminhoes/marcas";
        } else {
            System.out.println("Opção inválida");
            return;
        }

        var json = consumoApi.obterDados(url);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream().sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o codigo da marca que deseja consultar: ");
        var codigo = scan.nextLine();

        url = url + "/" + codigo + "/modelos";
        json = consumoApi.obterDados(url);
        var modeloLista = conversor.obterDados(json, Modelos.class);
        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nQual o nome do carro que deseja buscar? ");
        var nomeVeiculo = scan.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos Filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Informe o CODIGO do modelo para buscar as avaliações: ");
        var codigoModelo = scan.nextLine();
        url = url + "/" + codigoModelo + "/anos";
        json = consumoApi.obterDados(url);
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();
        for(int i = 0; i < anos.size(); i++){
            var enderecoAnos = url + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            var veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);

    }
}
