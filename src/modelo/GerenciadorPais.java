//Gabriel Silverio - 20106468

package modelo;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorPais {

    private ArrayList<Pais> listaPais;

    private GerenciadorPais(){
        listaPais = new ArrayList<Pais>();
    }

    private static GerenciadorPais instance;

    public static GerenciadorPais getInstance(){
        if(instance == null)
            instance = new GerenciadorPais();
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
                String nome = dados[1];
                Pais novo = new Pais(cod,nome);
                inserir(novo);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }

    public void inserir(Pais p){
        listaPais.add(p);
    }

    public Pais buscarCodigo(String codigo) {
        for(Pais p: listaPais) {
            if (p.getCodigo().equals(codigo))
                return p;
        }
        return null;
    }

    public ArrayList<Pais> listarTodas() {
        return new ArrayList<>(listaPais);
    }
}
