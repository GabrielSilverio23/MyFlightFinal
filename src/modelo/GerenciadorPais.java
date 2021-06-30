package modelo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorPais {

    /*private Map<String, Pais> pais;

    public GerenciadorPais() {
//        this.empresas = new HashMap<>();
//        this.empresas = new TreeMap<>();
        this.pais = new LinkedHashMap<>();
    }

    public ArrayList<Pais> listarTodas() {
        return new ArrayList<>(pais.values());
    }

    public void carregaDados(String nomeArq) throws IOException {
        Path path = Paths.get(nomeArq);
        try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("utf8")))) {
            sc.useDelimiter("[;\n]"); // separadores: ; e nova linha
            String header = sc.nextLine(); // pula cabeçalho
            String cod, nome;
            while (sc.hasNext()) {
                cod = sc.next();
                nome = sc.next();
                Pais nova = new Pais(cod, nome);
                adicionar(nova);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }

    public void adicionar(Pais pais1) {
        pais.put(pais1.getCodigo(),pais1);
    }

    public Pais buscarCodigo(String cod) {
        for(Pais p: pais.values())
            if(p.getCodigo().equals(cod))
                return p;
        return null;
        //return pais.get(cod);
    }*/
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
