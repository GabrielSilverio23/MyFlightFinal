package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorAeronaves {

    private ArrayList<Aeronave> listaAviao;

    private GerenciadorAeronaves(){
        listaAviao = new ArrayList<Aeronave>();
    }

    private static GerenciadorAeronaves instance;

    public static GerenciadorAeronaves getInstance(){
        if(instance == null)
            instance = new GerenciadorAeronaves();
        return instance;
    }

    public void carregaDados(String nomeArq) throws IOException {
        Path path = Paths.get(nomeArq);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("utf8"))) {
            String line = null;
            String header = reader.readLine();
            while ((line = reader.readLine())!=null) {
                String [] dados = line.split(";");
                String cod = dados[0];
                String desc = dados[1];
                int cap = Integer.parseInt(dados[2]);
                Aeronave novo = new Aeronave(cod,desc,cap);
                inserir(novo);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }

    public void inserir(Aeronave av){
        listaAviao.add(av);
    }

    public Aeronave buscarCodigo(String codigo) {
        for(Aeronave av: listaAviao) {
            if (av.getCodigo().equals(codigo))
                return av;
        }
        return null;
    }

    public ArrayList<Aeronave> listarTodas() {
        return new ArrayList<>(listaAviao);
    }

    public void ordenarCodigo() {
        listaAviao.sort( (Aeronave a1, Aeronave a2) ->
                a1.getCodigo().compareTo(a2.getCodigo()));
    }

    public void ordenarCodigoDescricao() {
        // Ordenando pelo código e desempatando pela descrição
        listaAviao.sort(Comparator.comparing(Aeronave::getCodigo).
                thenComparing(Aeronave::getDescricao));
    }
    public void ordenarDescricao() {
        // Usando Comparable<Aeronave> em Aeronave
        //Collections.sort(avioes);

        // Usando expressão lambda
        //avioes.sort( (Aeronave a1, Aeronave a2) ->
        //    a1.getDescricao().compareTo(a2.getDescricao()));

        // Mesma coisa, usando método static da interface Comparator:
        //avioes.sort(Comparator.comparing(a -> a.getDescricao()));

        // Invertendo o critério de comparação com reversed():
        listaAviao.sort(Comparator.comparing(Aeronave::getDescricao).reversed());
    }
}
