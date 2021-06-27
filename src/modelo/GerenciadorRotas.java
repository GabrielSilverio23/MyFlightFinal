package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GerenciadorRotas {

    /*private Map<String, Rota> rotas;

    public GerenciadorRotas() {
//        this.empresas = new HashMap<>();
//        this.empresas = new TreeMap<>();
        this.rotas = new LinkedHashMap<>();
    }

    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(rotas.values());
    }

    public void carregaDados(String nomeArq) throws IOException {
        Path path = Paths.get(nomeArq);
        try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("utf8")))) {
            sc.useDelimiter("[;\n ]"); // separadores: ; e nova linha
            String header = sc.nextLine(); // pula cabeçalho
            String cia, apo, apd, av, ignore;

            while (sc.hasNext()) {
                cia = sc.next();
                apo = sc.next();
                apd = sc.next();
                ignore = sc.next();
                ignore = sc.next();
                av = sc.next();
                GerenciadorCias ciaa = GerenciadorCias.getInstance();
                GerenciadorAeroportos ap = GerenciadorAeroportos.getInstance();
                GerenciadorAeronaves an = GerenciadorAeronaves.getInstance();

                Rota nova = new Rota(ciaa.buscarCodigo(cia), ap.buscarCodigo(apo), ap.buscarCodigo(apd), an.buscarCodigo(av));
                adicionar(nova);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }
    public void adicionar(Rota rota) {
        rotas.put(rota.getCia().getCodigo(),rota);
    }

    public Rota buscarCodigo(String cod) {
        return rotas.get(cod);
    }*/

    /*private ArrayList<Rota> rotas;

    public GerenciadorRotas() {
        this.rotas = new ArrayList<>();
    }

    public void ordenarCias() {
        Collections.sort(rotas);
    }

    public void ordenarNomesCias() {
        rotas.sort( (Rota r1, Rota r2) ->
                r1.getCia().getNome().compareTo(
                        r2.getCia().getNome()));
    }

    public void ordenarNomesAeroportos() {
        rotas.sort( (Rota r1, Rota r2) ->
                r1.getOrigem().getNome().compareTo(
                        r2.getOrigem().getNome()));
    }

    public void ordenarNomesAeroportosCias() {
        rotas.sort( (Rota r1, Rota r2) -> {
            int result = r1.getOrigem().getNome().compareTo(
                    r2.getOrigem().getNome());
            if(result != 0)
                return result;
            return r1.getCia().getNome().compareTo(
                    r2.getCia().getNome());
        });
    }
    public void adicionar(Rota r) {
        rotas.add(r);
    }

    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(rotas);
    }

    public ArrayList<Rota> buscarOrigem(String codigo) {
        ArrayList<Rota> result = new ArrayList<>();
        for(Rota r: rotas)
            if(r.getOrigem().getCodigo().equals(codigo))
                result.add(r);
        return result;
    }*/



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
        aeroportos = aeroportos.stream().distinct().collect(Collectors.toList());
        return aeroportos;
    }

    public List<Aeroporto> airportsByAirlinesTo(String cod){
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                aeroportos.add(r.getDestino());
        }
        aeroportos = aeroportos.stream().distinct().collect(Collectors.toList());
        return aeroportos;
    }

    public List<Aeroporto> frequency(String cod){
        for(Rota r: listaRotas) {
            if (r.getCia().getCodigo().equalsIgnoreCase(cod))
                aeroportos.add(r.getDestino());
        }
        aeroportos = aeroportos.stream().distinct().collect(Collectors.toList());
        return aeroportos;
    }


    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(listaRotas);
    }
}
