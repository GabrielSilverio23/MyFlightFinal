//Gabriel Silverio - 20106468

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
    HashMap<Aeroporto, HashSet<Aeroporto>> dicAeroporto = new HashMap<Aeroporto, HashSet<Aeroporto>>();

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
                    aux.add(novo);
                    dicRota.put(apOrig, aux);
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
    //retorna uma lista de aeroportos
    public List<Aeroporto> airportsByAirlinesFrom(String cod){
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                aeroportos.add(r.getOrigem());
        }
        return aeroportos;
    }
    //retorna uma rota
    public Rota buscaRota(Aeroporto apPartida, Aeroporto apChegada){
        for(Rota r: dicRota.get(apPartida)){
            if(r.getDestino()==apChegada)
                return r;
        }
        return null;
    }
    //Cria o dicionario de aeroporto, que será usado para manipular as rotas;
    public HashMap<Aeroporto, HashSet<Aeroporto>> criaDicAp(){
        for(Rota r:listaRotas) {
            if (dicAeroporto.containsKey(r.getOrigem())) {
                HashSet<Aeroporto> aux = dicAeroporto.get(r.getOrigem());
                aux.add(r.getDestino());
                dicAeroporto.put(r.getOrigem(), aux);
            } else {
                HashSet<Aeroporto> aux = new HashSet<>();
                aux.add(r.getDestino());
                dicAeroporto.put(r.getOrigem(), aux);
            }
        }
        return dicAeroporto;
    }

    //retorna 20 rotas possíveis entre um ponto e outro, ordenados pelo numero de conexões e tempo e voo
    public List<RotaEscala> possiveisRotas(Aeroporto apPartida, Aeroporto apChegada){
        listaEscala.clear();
        criaDicAp();
        if(dicAeroporto.get(apPartida).contains(apChegada)){
            RotaEscala r = new RotaEscala(buscaRota(apPartida, apChegada));
            listaEscala.add(r);
        }

        for(Aeroporto ap: dicAeroporto.get(apPartida)){
            if(dicAeroporto.get(ap).contains(apChegada)){
                RotaEscala r = new RotaEscala(buscaRota(apPartida, ap));
                //r.addRota(r);
                r.addRota(buscaRota(ap, apChegada));
                listaEscala.add(r);
            }
        }

        for(Aeroporto ap: dicAeroporto.get(apPartida)){
            for(Aeroporto ap1: dicAeroporto.get(ap)) {
                if (dicAeroporto.get(ap1).contains(apChegada) && ap1!=apPartida && ap!=apChegada ) {
                    RotaEscala r = new RotaEscala(buscaRota(apPartida, ap));
                    r.addRota(buscaRota(ap, ap1));
                    r.addRota(buscaRota(ap1, apChegada));
                    listaEscala.add(r);
                }
            }
        }
//
        Collections.sort(listaEscala, new Comparator<RotaEscala>() {
            @Override
            public int compare(RotaEscala um, RotaEscala outro){
                int comp = um.getNumeroRota() - outro.getNumeroRota();
                if(comp == 0){
                    comp = Double.compare(um.getDuracao(), outro.getDuracao());
                }
                return comp;
            }
        });
        return listaEscala.stream().limit(20).collect(Collectors.toList());
    }

    //retorna 20 rotas possíveis entre um ponto e outro, mas desta vez com uma parada de escolha do usuario
    // ordenados pelo numero de conexões e tempo e voo
    public List<RotaEscala> possiveisRotasEscala(Aeroporto apPartida, Aeroporto apEscala,Aeroporto apChegada){
        listaEscala.clear();
        criaDicAp();
        if(dicAeroporto.get(apPartida).contains(apEscala) && dicAeroporto.get(apEscala).contains(apChegada)){
            RotaEscala r = new RotaEscala(buscaRota(apPartida, apEscala));
            r.addRota(buscaRota(apEscala,apChegada));
            listaEscala.add(r);
        }
        if(dicAeroporto.get(apPartida).contains(apEscala)){
            for(Aeroporto ap: dicAeroporto.get(apEscala)){
                if(dicAeroporto.get(ap).contains(apChegada)){
                    RotaEscala r = new RotaEscala(buscaRota(apPartida, apEscala));
                    r.addRota(buscaRota(apEscala,ap));
                    r.addRota(buscaRota(ap,apChegada));
                    listaEscala.add(r);
                }
            }
        }else{
            for(Aeroporto ap: dicAeroporto.get(apPartida)){
                if(dicAeroporto.get(ap).contains(apEscala)){
                    if(dicAeroporto.get(apEscala).contains(apChegada)) {
                        RotaEscala r = new RotaEscala(buscaRota(apPartida, ap));
                        r.addRota(buscaRota(ap, apEscala));
                        r.addRota(buscaRota(apEscala, apChegada));
                        listaEscala.add(r);
                    }
                }
            }
        }
        if(dicAeroporto.get(apPartida).contains(apEscala)){
            for(Aeroporto ap: dicAeroporto.get(apEscala)){
                for(Aeroporto ap1: dicAeroporto.get(ap)){
                    if(dicAeroporto.get(ap1) != null){
                        if(dicAeroporto.get(ap1).contains(apChegada)){
                            RotaEscala r = new RotaEscala(buscaRota(apPartida, apEscala));
                            r.addRota(buscaRota(apEscala,ap));
                            r.addRota(buscaRota(ap,ap1));
                            r.addRota(buscaRota(ap1,apChegada));
                            listaEscala.add(r);
                        }
                    }
                }
            }
        }else{
            for(Aeroporto ap: dicAeroporto.get(apPartida)){
                if(dicAeroporto.get(ap).contains(apEscala)){
                    for(Aeroporto ap1:dicAeroporto.get(apEscala)){
                        if(dicAeroporto.get(ap1).contains(apChegada)){
                            RotaEscala r = new RotaEscala(buscaRota(apPartida, ap));
                            r.addRota(buscaRota(ap, apEscala));
                            r.addRota(buscaRota(apEscala,ap1));
                            r.addRota(buscaRota(ap1,apChegada));
                            listaEscala.add(r);
                        }
                    }
                }else{
                    for(Aeroporto ap1:dicAeroporto.get(ap)){
                        if(dicAeroporto.get(ap1).contains(apEscala) && dicAeroporto.get(apEscala).contains(apChegada)){
                            RotaEscala r = new RotaEscala(buscaRota(apPartida, ap));
                            r.addRota(buscaRota(ap, ap1));
                            r.addRota(buscaRota(ap1,apEscala));
                            r.addRota(buscaRota(apEscala,apChegada));
                            listaEscala.add(r);
                        }
                    }
                }
            }
        }
//
        Collections.sort(listaEscala, new Comparator<RotaEscala>() {
            @Override
            public int compare(RotaEscala um, RotaEscala outro){
                int comp = um.getNumeroRota() - outro.getNumeroRota();
                if(comp == 0){
                    comp = Double.compare(um.getDuracao(), outro.getDuracao());
                }
                return comp;
            }
        });
        return listaEscala.stream().limit(20).collect(Collectors.toList());
    }
    //retorna uma lista com os aeroportos acessiveis de acordo com o limite de voo
    public List<RotaEscala> possiveisDestino(Aeroporto apPartida, Double hora){
        listaEscala.clear();
        criaDicAp();

        for(Aeroporto ap:dicAeroporto.get(apPartida)){
            if(buscaRota(apPartida,ap).getDuracao()<=hora){
                RotaEscala x = new RotaEscala(buscaRota(apPartida,ap));
                listaEscala.add(x);
            }
            for(Aeroporto ap1:dicAeroporto.get(ap)){
                double h=0;
                h = buscaRota(apPartida,ap).getDuracao() + buscaRota(ap,ap1).getDuracao();
                if(h<=hora && ap1!=apPartida) {
                    RotaEscala x1 = new RotaEscala(buscaRota(apPartida,ap));
                    x1.addRota(buscaRota(ap, ap1));
                    listaEscala.add(x1);
                }
                if(dicAeroporto.get(ap1) != null){
                    for(Aeroporto ap2: dicAeroporto.get(ap1)){
                        double j=0;
                        j = buscaRota(apPartida,ap).getDuracao() + buscaRota(ap,ap1).getDuracao() + buscaRota(ap1,ap2).getDuracao();
                        if(j<=hora && ap2!=apPartida && ap2!=ap1) {
                            RotaEscala x2 = new RotaEscala(buscaRota(apPartida,ap));
                            x2.addRota(buscaRota(ap, ap1));
                            x2.addRota(buscaRota(ap1, ap2));
                            listaEscala.add(x2);
                        }
                    }
                }
            }
        }

        return listaEscala;
    }
    //retorna uma lista de rota
    public List<Rota> listaRota2(String cod){
        List<Rota> lr = new ArrayList<>();
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                lr.add(r);
        }
        return lr;
    }
    //lista todas as rotas
    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(listaRotas);
    }
    //observableList usado para puxar dados para o comboBox no main
    public ObservableList listaRota(String cod){
        List<Pais> lr = new ArrayList<>();
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                lr.add(r.getOrigem().getPais());
        }
        lr = lr.stream().distinct().collect(Collectors.toList());
        return FXCollections.observableList(lr);
    }
    //observableList usado para puxar dados para o comboBox no main
    public ObservableList listaRotaF(String apPartida, String apChegada){
        GerenciadorAeroportos gerAp = GerenciadorAeroportos.getInstance();
        List<RotaEscala> lr = possiveisRotas(gerAp.buscarCodigo(apPartida), gerAp.buscarCodigo(apChegada));
        lr = lr.stream().distinct().collect(Collectors.toList());
        return FXCollections.observableList(lr);
    }
    public ObservableList listaRotaEscala(String apPartida, String apEscala, String apChegada){
        GerenciadorAeroportos gerAp = GerenciadorAeroportos.getInstance();
        List<RotaEscala> lr = possiveisRotasEscala(gerAp.buscarCodigo(apPartida), gerAp.buscarCodigo(apEscala), gerAp.buscarCodigo(apChegada));
        Collections.sort(lr, new Comparator<RotaEscala>() {
            @Override
            public int compare(RotaEscala um, RotaEscala outro){
                int comp = um.getNumeroRota() - outro.getNumeroRota();
                if(comp == 0){
                    comp = Double.compare(um.getDuracao(), outro.getDuracao());
                }
                return comp;
            }
        });
        return FXCollections.observableList(lr);
    }
}
