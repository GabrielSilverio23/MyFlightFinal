package modelo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorAeroportos {

    /*private ArrayList<Aeroporto> aeroportos;

    public GerenciadorAeroportos() {
        this.aeroportos = new ArrayList<>();
    }

    public void ordenarNomes() {
        Collections.sort(aeroportos);
    }

    public void adicionar(Aeroporto aero) {
        aeroportos.add(aero);
    }*/

    private Map<String, Aeroporto> aeroportos;

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
        try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("utf8")))) {
            sc.useDelimiter("[;\n]"); // separadores: ; e nova linha
            String header = sc.nextLine(); // pula cabeçalho
            String cod, nome, pais, lat, lon;
            //double lat, lon;
            while (sc.hasNext()) {
                cod = sc.next();
                lat = sc.next();
                lon = sc.next();
                nome = sc.next();
                pais = sc.next();
                GerenciadorPais gp = new GerenciadorPais();
                Pais p = new Pais(gp.buscarCodigo(pais).getCodigo(), gp.buscarCodigo(pais).getNome());
                Aeroporto nova = new Aeroporto(cod, nome, new Geo(Double.parseDouble(lat),Double.parseDouble(lon)), p);
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
