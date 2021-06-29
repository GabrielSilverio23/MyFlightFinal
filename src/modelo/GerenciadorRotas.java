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
    List<RotaEscala> listaEscala = new ArrayList<>();
    HashMap<Aeroporto, HashSet<Rota>> dicRota = new HashMap<Aeroporto, HashSet<Rota>>();

    private GerenciadorRotas(){
        listaRotas = new ArrayList<Rota>();
    }

    private static GerenciadorRotas instance;

    public static GerenciadorRotas getInstance(){
        if(instance == null)
            instance = new GerenciadorRotas();
        return instance;
    }

//    public void carregaDados(String nomeArq) throws IOException {
//        HashMap<CiaAerea, HashSet<Aeroporto>> aps = new HashMap<CiaAerea, HashSet<Aeroporto>>();
//        Path path = Paths.get(nomeArq);
//        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("utf8"))) {
//            String line = null;
//            String header = reader.readLine();
//            while ((line = reader.readLine())!=null) {
//                String [] dados = line.split(";");
//                String cia = dados[0];
//                String apo = dados[1];
//                String apd = dados[2];
//                String av = dados[5].substring(0,3);
//                GerenciadorCias gcia = GerenciadorCias.getInstance();
//                GerenciadorAeroportos gap = GerenciadorAeroportos.getInstance();
//                GerenciadorAeronaves gav = GerenciadorAeronaves.getInstance();
//                Rota novo = new Rota(gcia.buscarCodigo(cia), gap.buscarCodigo(apo), gap.buscarCodigo(apd), gav.buscarCodigo(av));
//                inserir(novo);
//            }
//        }
//    }

    public void carregaDados(String nomeArq) throws IOException {
        //HashMap<CiaAerea, HashSet<Aeroporto>> aps = new HashMap<CiaAerea, HashSet<Aeroporto>>();
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
                CiaAerea c = gcia.buscarCodigo(cia);
                Aeroporto apOrig = gap.buscarCodigo(apo);
                Aeroporto apDest = gap.buscarCodigo(apd);
                Aeronave avNav = gav.buscarCodigo(av);
                Rota novo = new Rota(c, apOrig, apDest, avNav);
                if (dicRota.containsKey(apOrig)){
                    HashSet<Rota> aux = dicRota.get(apOrig);
                    for(Rota a:aux){
                        if(a.getDestino()==novo.getDestino() && a.getOrigem()==novo.getOrigem()){
                            break;
                        }else{
                            aux.add(novo);
                            dicRota.put(apOrig, aux);
                            break;
                        }
                    }
                }else{
                    HashSet<Rota> aux = new HashSet<>();
                    aux.add(novo);
                    dicRota.put(apOrig,aux);
                }
                inserir(novo);
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

    public List<RotaEscala> possiveisRotas(Aeroporto apPartida, Aeroporto apChegada){
        for(Rota r: dicRota.get(apPartida)){
            if(r.getOrigem() == apPartida && r.getDestino() == apChegada) {
                RotaEscala r1 = new RotaEscala(r);
                listaEscala.add(r1);
                break;
            }
        }
        for(Rota app: dicRota.get(apPartida)){
            for(Rota apc: dicRota.get(app.getDestino())) {
                if (apChegada == apc.getDestino()){
                    dicRota.get(apPartida).remove(apc);
                    RotaEscala r1 = new RotaEscala(app);
                    r1.addRota(apc);
                    listaEscala.add(r1);
                    break;
                }
            }
        }
        for(Rota app: dicRota.get(apPartida)){
            for(Rota app1: dicRota.get(app.getDestino())){
                for(Rota app2: dicRota.get(app1.getDestino())) {
                    if (app.getDestino()==app1.getOrigem() && app1.getDestino() == app2.getOrigem() && app2.getDestino() == apChegada) {
                        RotaEscala r2 = new RotaEscala(app);
                        r2.addRota(app1);
                        r2.addRota(app2);
                        listaEscala.add(r2);
                        break;
                    }
                }
            }
        }

        return listaEscala.stream().limit(20).collect(Collectors.toList());
//        return listaEscala;
    }

    public List<RotaEscala> verificaDup(Aeroporto apPartida, Aeroporto apChegada){
        possiveisRotas(apPartida, apChegada);

        listaEscala = listaEscala.stream().distinct().collect(Collectors.toList());
        return listaEscala;
    }



    public List<Rota> listaRota2(String cod){
        List<Rota> lr = new ArrayList<>();
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                lr.add(r);
        }
        return lr;
    }

//    public HashMap<Aeroporto,HashSet<Aeroporto>> dicionarioAeroporto(){
//        HashMap<Aeroporto, HashSet<Aeroporto>> dicRota = new HashMap<Aeroporto, HashSet<Aeroporto>>();
//        for(Rota r: listaRotas) {
//            if (dicRota.containsKey(r.getOrigem())) {
//                HashSet<Aeroporto> aux = dicRota.get(r.getOrigem());
//                aux.add(r.getDestino());
//                dicRota.put(r.getOrigem(), aux);
//            }else{
//                HashSet<Aeroporto> aux = new HashSet<>();
//                aux.add(r.getDestino());
//                dicRota.put(r.getOrigem(),aux);
//            }
//        }
//        return dicRota;
//    }

    public List<Rota> aux(String apPartida, String apChegada){
        List<Rota> lst = new ArrayList<>();
        //RotaEscala resultado = new RotaEscala(possivelRotaSemEscala(apPartida,apChegada));
        for(Rota r: listaRotas){
            if(r.getOrigem().getCodigo().equalsIgnoreCase(apPartida) || r.getDestino().getCodigo().equalsIgnoreCase(apChegada)){
                lst.add(r);
            }
        }
        return lst;//listaUmaEscala;
    }

    public List<Rota> aux2(String apPartida, String apChegada){
        List<Rota> lst = aux(apPartida,apChegada);
        List<Rota> lst1 = new ArrayList<>();
        //RotaEscala resultado = new RotaEscala(possivelRotaSemEscala(apPartida,apChegada));
        for(Rota r: listaRotas){
            for(Rota r1:lst){
                if(r1.getOrigem().getCodigo().equalsIgnoreCase(apPartida) && r1.getDestino().getCodigo().equalsIgnoreCase(r.getOrigem().getCodigo())
                && r.getDestino().getCodigo().equalsIgnoreCase(apChegada)){
                    lst1.add(r);
                    lst1.add(r1);
                }
            }
        }
        return lst1;//listaUmaEscala;
    }

    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(listaRotas);
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
}
