//Gabriel Silverio - 20106468

package modelo;

import java.util.ArrayList;
import java.util.List;

public class RotaEscala extends Rota implements Comparable<Rota>{
    private List<Rota> rotas;

    public RotaEscala(Rota rota){
        super(rota.getCia(), rota.getOrigem(), rota.getDestino(), rota.getAeronave());
        rotas = new ArrayList<Rota>();
    }
    public RotaEscala(CiaAerea cia, Aeroporto ori, Aeroporto dest, Aeronave nav){
        super(cia, ori, dest, nav);
        rotas = new ArrayList<Rota>();
    }

    public void addRota(Rota escala){
        rotas.add(escala);
    }

    public List<Rota> getRotas(){
        return rotas;
    }

    public double getDuracao() {
        double distancia = calcDistancia();

        double velocidade = 805.1;

        double duracaoH = 0.5+distancia*1.0/velocidade;
        if(rotas!=null){
            for (Rota escala : rotas) {
                duracaoH += escala.getDuracao();
            }
        }

        duracaoH += rotas.size() * 0.5;
        return duracaoH;
    }

    public int getNumeroRota(){
        int i=0;
        for(Rota escala: rotas)
            i++;
        return i;
    }

    @Override
    public String toString() {
        String rotaStr = "";
        for(Rota escala: rotas)
            rotaStr+= String.format("%s\n",
                    escala);

        return String.format(" %s \n %s\n Duração Total: %.2f",
                super.toString(), rotaStr, getDuracao());
    }
}
