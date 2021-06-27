package modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.ListChangeListener;
public class PaisesData{

    private static PaisesData cbp;

    static {
        try {
            cbp = new PaisesData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> listaPais;
    //private List<Integer> qtdadeAssentos;

    private List<String> loadPais() throws IOException {
        GerenciadorPais gerPais = GerenciadorPais.getInstance();
        gerPais.carregaDados("countries.dat");
        List<Pais> p = gerPais.listarTodas();
        List<String> lst = new LinkedList<String>();
        for(Pais lp: p){
            lst.add(lp.getNome());
        }
        return lst;
    }

//    private List<Integer> loadQtdadeAssentos(){
//        List<Integer> lst = new LinkedList<>();
//        for(int i=1;i<10;i++){
//            lst.add(i);
//        }
//        return lst;
//    }

    private PaisesData() throws IOException {
        listaPais = loadPais();
    }

    public static PaisesData getInstance(){
        return(cbp);
    }

    public ObservableList getListaPais(){
        return FXCollections.observableList(listaPais);
    }

}
