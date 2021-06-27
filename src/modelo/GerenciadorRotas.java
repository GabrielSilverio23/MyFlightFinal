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

public class GerenciadorRotas {

    //private static GerenciadorRotas cbp = new GerenciadorRotas();

    private ArrayList<Rota> listaRotas;
    List<Aeroporto> aeroportos = new ArrayList<>();

    private GerenciadorRotas(){
        listaRotas = new ArrayList<Rota>();
    }

    private static GerenciadorRotas instance;

    public static GerenciadorRotas getInstance(){
        if(instance == null)
            instance = new GerenciadorRotas();
        return instance;
    }

    public void carregaDados(String nomeArq) throws IOException {
        HashMap<CiaAerea, HashSet<Aeroporto>> aps = new HashMap<CiaAerea, HashSet<Aeroporto>>();
        Path path = Paths.get(nomeArq);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("utf8"))) {
            String line = null;
            String header = reader.readLine();
            while ((line = reader.readLine())!=null) {
                String [] dados = line.split(";");
                String cia = dados[0];
                String apo = dados[1];
                String apd = dados[2];
                String av = dados[5].substring(0,3);
                GerenciadorCias gcia = GerenciadorCias.getInstance();
                GerenciadorAeroportos gap = GerenciadorAeroportos.getInstance();
                GerenciadorAeronaves gav = GerenciadorAeronaves.getInstance();
//                if(aps.containsKey(apo))
//                    (aps.get(apo)).add(gap.buscarCodigo(apd));
//                else{
//                    HashSet<Aeroporto> aux = new HashSet<>();
//                    aux.add(gap.buscarCodigo(apd));
//                    aps.put(gcia.buscarCodigo(apo), aux);
//                }
                Rota novo = new Rota(gcia.buscarCodigo(cia), gap.buscarCodigo(apo), gap.buscarCodigo(apd), gav.buscarCodigo(av));
                inserir(novo);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }

    public void inserir(Rota p){
        listaRotas.add(p);
    }

    public Rota buscarCia(String codigo) {
        for(Rota r: listaRotas) {
            if (codigo.equalsIgnoreCase(r.getCia().getCodigo()))
                return r;
        }
        return null;
    }
    public Rota buscarOrigem(String codigo) {
        for(Rota r: listaRotas) {
            if (codigo.equalsIgnoreCase(r.getOrigem().getCodigo()))
                return r;
        }
        return null;
    }
    public Rota buscarDestino(String codigo) {
        for(Rota r: listaRotas) {
            if (codigo.equalsIgnoreCase(r.getDestino().getCodigo()))
                return r;
        }
        return null;
    }
    public Rota buscarAeronave(String codigo) {
        for(Rota r: listaRotas) {
            if (codigo.equalsIgnoreCase(r.getAeronave().getCodigo()))
                return r;
        }
        return null;
    }



    public List<Aeroporto> airportsByAirlinesFrom(String cod){
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                aeroportos.add(r.getOrigem());
        }
//        aeroportos = aeroportos.stream().distinct().collect(Collectors.toList());
        return aeroportos;
    }

    public List<Aeroporto> airportsByAirlinesTo(String cod){
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                aeroportos.add(r.getDestino());
        }
//        aeroportos = aeroportos.stream().distinct().collect(Collectors.toList());
        return aeroportos;
    }


    public ObservableList listaRota(String cod){
        List<Pais> lr = new ArrayList<>();
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                lr.add(r.getOrigem().getPais());
        }
        lr = lr.stream().distinct().collect(Collectors.toList());
        return FXCollections.observableList(lr);
    }

    public List<Rota> listaRota2(String cod){
        List<Rota> lr = new ArrayList<>();
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                lr.add(r);
        }
        return lr;
    }


    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(listaRotas);
    }



    public List<Pais> loadPais(String cod, String sentido){
        List<Pais> pais = new LinkedList<Pais>();

        if(sentido.equalsIgnoreCase("partida")){
            for(Rota r: listaRotas) {
                if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                    pais.add(r.getOrigem().getPais());
            }
            pais = pais.stream().distinct().collect(Collectors.toList());
            return pais;
        }else{
            for(Rota r: listaRotas) {
                if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                    pais.add(r.getDestino().getPais());
            }
            pais = pais.stream().distinct().collect(Collectors.toList());
            return pais;
        }
    }
//
//    private List<String> paises;
//
//    private GerenciadorRotas(String cod, String sentido){
//        paises = loadPais(cod, sentido);
//    }
//
//    public ObservableList getPais(){
//        return FXCollections.observableList(paises);
//    }
}
