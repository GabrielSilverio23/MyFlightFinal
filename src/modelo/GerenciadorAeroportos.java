package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorAeroportos {

    /*private Map<String, Aeroporto> aeroportos;

    public GerenciadorAeroportos() {
//        this.empresas = new HashMap<>();
//        this.empresas = new TreeMap<>();
        this.aeroportos = new LinkedHashMap<>();
    }

    public ArrayList<Aeroporto> listarTodas() {
        return new ArrayList<>(aeroportos.values());
    }

    public void carregaDados(String nomeArq) throws IOException {
        Path path = Paths.get(nomeArq);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("utf8"))) {
            String line = null;
            String header = reader.readLine();
            while ((line = reader.readLine())!=null) {
                String [] dados = line.split(";");
                String cod = dados[0];
                double lat = Double.parseDouble(dados[1]);
                double lon = Double.parseDouble(dados[2]);
                String nome = dados[3];
                String pais = dados[4];
                GerenciadorPais gp = GerenciadorPais.getInstance();
                //Pais x = gp.buscarCodigo(pais);
                Aeroporto nova = new Aeroporto(cod, nome, new Geo(lat, lon), gp.buscarCodigo(pais));
                adicionar(nova);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }
    public void adicionar(Aeroporto ap1) {
        aeroportos.put(ap1.getCodigo(),ap1);
    }

    public Aeroporto buscarCodigo(String cod) {
        return aeroportos.get(cod);
    }*/

    private ArrayList<Aeroporto> listaAeroporto;

    private GerenciadorAeroportos(){
        listaAeroporto = new ArrayList<Aeroporto>();
    }

    private static GerenciadorAeroportos instance;

    public static GerenciadorAeroportos getInstance(){
        if(instance == null)
            instance = new GerenciadorAeroportos();
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
                double lat = Double.parseDouble(dados[1]);
                double lon = Double.parseDouble(dados[2]);
                String nome = dados[3];
                String pais = dados[4];
                GerenciadorPais gp = GerenciadorPais.getInstance();
                Aeroporto novo = new Aeroporto(cod, nome, new Geo(lat, lon), gp.buscarCodigo(pais));
                inserir(novo);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }

    public void inserir(Aeroporto p){
        listaAeroporto.add(p);
    }

    public Aeroporto buscarCodigo(String codigo) {
        for(Aeroporto p: listaAeroporto) {
            if (p.getCodigo().equals(codigo))
                return p;
        }
        return null;
    }

    public ArrayList<Aeroporto> listarTodas() {
        return new ArrayList<>(listaAeroporto);
    }

    /*public ArrayList<Aeroporto> listarTodos() {
        return new ArrayList<>(aeroportos);
    }

    public Aeroporto buscarCodigo(String codigo) {
        for(Aeroporto a: aeroportos)
            if(a.getCodigo().equals(codigo))
                return a;
        return null;
    }*/
}
