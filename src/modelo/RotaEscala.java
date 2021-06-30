package modelo;

import java.util.ArrayList;
import java.util.List;

public class RotaEscala extends Rota implements Comparable<Rota>{
    private List<Rota> rotas;

    public RotaEscala(Rota rota){
        super(rota.getCia(), rota.getOrigem(), rota.getDestino(), rota.getAeronave());
        rotas = new ArrayList<Rota>();
    }

    public void addRota(Rota escala){
        rotas.add(escala);
    }


    public List<Rota> getRotas(){
        return rotas;
    }

    public boolean verificaRotasDuplas(Aeroporto ap){
        int i=0;
        for(Rota r: rotas){
            if(r.getDestino() == ap){
                i++;
                if(i>=2){
                    return false;
                }
            }else if(r.getOrigem() == ap){
                i++;
                if(i>=2){
                    return false;
                }
            }
        }
        return true;
    }

    public double getDuracao(){
        double duracaoT = 0;
        for(Rota escala: rotas){
            duracaoT = duracaoT + escala.getDuracao();
        }

        duracaoT=duracaoT+rotas.size()*0.5;
        return duracaoT;
    }

    public int getNumeroRota(){
        int i=0;

        for(Rota escala: rotas){
            i++;
        }
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
