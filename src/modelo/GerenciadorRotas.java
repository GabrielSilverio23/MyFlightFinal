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

    public List<Rota> escala(String apPartida,String apChegada){
        List<Rota> lr = new ArrayList<>();
        for(Rota r: listaRotas){
            if(r.getOrigem().getCodigo().equalsIgnoreCase(apPartida) || r.getDestino().getCodigo().equalsIgnoreCase(apChegada)){
                lr.add(r);
            }
            for(Rota r2: listaRotas){
                if(r.getOrigem().getCodigo().equalsIgnoreCase(apPartida) && r.getDestino().getCodigo().equalsIgnoreCase(r2.getOrigem().getCodigo())){
                    lr.add(r2);
                    lr.add(r);
                }else if(r.getDestino().getCodigo().equalsIgnoreCase(apChegada) && r.getOrigem().getCodigo().equalsIgnoreCase(r2.getDestino().getCodigo())){
                    lr.add(r2);
                    lr.add(r);
                }
            }
        }
        return lr;
    }

    public List<Rota> possiveisRotas(String apPartida, String apChegada){
        List<Rota> lr = new ArrayList<>();
        List<Rota> rot = escala(apPartida,apChegada);
        int i=0;
        for(Rota r1: rot){
            if (r1.getOrigem().getCodigo().equalsIgnoreCase(apPartida) && r1.getDestino().getCodigo().equalsIgnoreCase(apChegada)) {
                lr.add(r1);
                i++;
                if(i==20){
                    return lr;
                }
            }
        }
        for(Rota r2: rot) {
            for(Rota aux: rot){
                if (r2.getOrigem().getCodigo().equalsIgnoreCase(apPartida) && aux.getDestino().getCodigo().equalsIgnoreCase(apChegada) &&
                        r2.getDestino().getCodigo().equalsIgnoreCase(aux.getOrigem().getCodigo())) {
                    lr.add(aux);
                    lr.add(r2);
                    i++;
                    if(i==20)
                        return lr;
                }
            }
        }
        for(Rota r4: rot) {
            for(Rota r5: rot) {
                for (Rota r6 : rot) {
                    if (r4.getOrigem().getCodigo().equalsIgnoreCase(apPartida) && r4.getDestino().getCodigo().equalsIgnoreCase(r5.getOrigem().getCodigo())) {
                        if (r5.getDestino().getCodigo().equalsIgnoreCase(r6.getOrigem().getCodigo()) && r6.getDestino().getCodigo().equalsIgnoreCase(apChegada)) {
                            lr.add(r4);
                            lr.add(r5);
                            lr.add(r6);
                            i++;
                            if(i==20)
                                return lr;
                        }
                    }
                }
            }
        }

        lr = lr.stream().distinct().collect(Collectors.toList());
        return lr;
    }


    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(listaRotas);
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
