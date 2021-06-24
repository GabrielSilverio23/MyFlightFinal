package modelo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorRotas {

    private Map<String, Rota> rotas;

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
                GerenciadorCias ciaa = new GerenciadorCias();
                GerenciadorAeroportos ap = new GerenciadorAeroportos();
                GerenciadorAeronaves an = new GerenciadorAeronaves();

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
    }

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
}
