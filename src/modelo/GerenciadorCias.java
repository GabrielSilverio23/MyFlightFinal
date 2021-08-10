//Gabriel Silverio - 20106468

package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorCias {

    private ArrayList<CiaAerea> listaCia;

    private GerenciadorCias(){
        listaCia = new ArrayList<CiaAerea>();
    }

    private static GerenciadorCias instance;

    public static GerenciadorCias getInstance(){
        if(instance == null)
            instance = new GerenciadorCias();
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
                CiaAerea novo = new CiaAerea(cod,nome);
                inserir(novo);
                //System.out.format("%s - %s (%s)%n", nome, data, cpf);
            }
        }
    }

    public void inserir(CiaAerea ca){
        listaCia.add(ca);
    }

    public CiaAerea buscarCodigo(String codigo) {
        for(CiaAerea ca: listaCia) {
            if (ca.getCodigo().equals(codigo))
                return ca;
        }
        return null;
    }


    public ArrayList<CiaAerea> listarTodas() {
        return new ArrayList<>(listaCia);
    }
}
