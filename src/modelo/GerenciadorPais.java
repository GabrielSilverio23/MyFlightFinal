package modelo;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorPais {

    private Map<String, Pais> pais;

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
        return pais.get(cod);
    }
}
