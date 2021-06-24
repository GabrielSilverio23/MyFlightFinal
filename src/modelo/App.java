package modelo;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) {

        //le arquivo airlines.dat
        GerenciadorCias gerCias = GerenciadorCias.getInstance();

        try {
            gerCias.carregaDados("airlines.dat");
        } catch (IOException e) {
            System.out.println("Não foi possível ler airlines.dat!");
//            System.exit(1);
        }
        ArrayList<CiaAerea> todasCias = gerCias.listarTodas();
        System.out.println("Total cias:"+todasCias.size());
//        for(CiaAerea cia: todasCias)
//            System.out.println(cia.getCodigo()+" - "+cia.getNome());




        //le arquivo equipment.dat
        GerenciadorAeronaves gerAvioes = GerenciadorAeronaves.getInstance();
        try {
            gerAvioes.carregaDados("equipment.dat");
        } catch (IOException e) {
            System.out.println("Não foi possível ler equipment.dat!");
//            System.exit(1);
        }
        ArrayList<Aeronave> todasAeronaves = gerAvioes.listarTodas();
        System.out.println("Total aeronaves:"+todasAeronaves.size());
//        for(Aeronave nav: todasAeronaves)
//            System.out.println(nav.getCodigo()+" - "+nav.getDescricao()+" - "+nav.getCapacidade());




        //le o arquivo countries.dat
        GerenciadorPais gp = GerenciadorPais.getInstance();

        try {
            gp.carregaDados("countries.dat");
        } catch (IOException e) {
            System.out.println("Não foi possível ler countries.dat!");
//            System.exit(1);
        }
        ArrayList<Pais> todosPaises = gp.listarTodas();
        System.out.println("Total Paises:"+todosPaises.size());
//        for(Pais p: todosPaises)
//            System.out.println(p.getCodigo()+" - "+p.getNome());





        //le o arquivo airports.dat
        GerenciadorAeroportos gerAero = GerenciadorAeroportos.getInstance();
        try {
            gerAero.carregaDados("airports.dat");
        } catch (IOException e) {
            System.out.println("Não foi possível ler airports.dat!");
//            System.exit(1);
        }
        ArrayList<Aeroporto> todosAeroportos = gerAero.listarTodas();
        System.out.println("Total aeroportos:"+todosAeroportos.size());
//        for(Aeroporto ap: todosAeroportos)
//            System.out.println(ap.getCodigo()+" - "+ap.getNome()+" - "+ap.getLocal()+" - "+ap.getPais().getCodigo());




        //le o arquivo routes.dat
        GerenciadorRotas gerRotas = GerenciadorRotas.getInstance();

        try {
            gerRotas.carregaDados("routes.dat");
        } catch (IOException e) {
            System.out.println("Não foi possível ler airports.dat!");
//            System.exit(1);
        }
        ArrayList<Rota> todasRotas = gerRotas.listarTodas();
        System.out.println("Total rotas:"+todasRotas.size());
//        for(Rota rt: todasRotas)
//            System.out.println(rt.getCia().getCodigo()+" - "+rt.getOrigem().getCodigo()+" - "+rt.getDestino().getCodigo()+" - "+rt.getAeronave().getCodigo());




/*
        Rota poagru = new Rota(latam, poa, gru, b733);
        Rota grupoa = new Rota(latam, gru, poa, b733);
        Rota grumia = new Rota(tap, gru, mia, a380);
        Rota grulis = new Rota(tap, gru, lis, a380);

        gerRotas.adicionar(grumia);
        gerRotas.adicionar(grulis);
        gerRotas.adicionar(poagru);
        gerRotas.adicionar(grupoa);
//		gerRotas.ordenarCias();
        gerRotas.ordenarNomesAeroportosCias();

        System.out.println("\nRotas ordenadas:\n");
        for(Rota r: gerRotas.listarTodas())
            System.out.println(r);
        System.out.println();

        LocalDateTime manhacedo = LocalDateTime.of(2018, 3, 29, 8, 0);
        LocalDateTime manhameio = LocalDateTime.of(2018, 4, 4, 10, 0);
        LocalDateTime tardecedo = LocalDateTime.of(2018, 4, 4, 14, 30);
        LocalDateTime tardetarde = LocalDateTime.of(2018, 4, 5, 17, 30);

        Duration curto = Duration.ofMinutes(90);
        Duration longo1 = Duration.ofHours(12);
        Duration longo2 = Duration.ofHours(14);

        GerenciadorVoos gerVoos = new GerenciadorVoos();

        gerVoos.adicionar(new Voo(poagru, curto)); // agora!
        gerVoos.adicionar(new Voo(grulis, tardecedo, longo2));
        gerVoos.adicionar(new Voo(grulis, tardetarde, longo2));
        gerVoos.adicionar(new Voo(poagru, manhacedo, curto));
        gerVoos.adicionar(new Voo(grupoa, manhameio, curto));
        gerVoos.adicionar(new Voo(grumia, manhacedo, longo1));

        // Vôo com várias escalas
        VooEscalas vooEsc = new VooEscalas(poagru,
                manhacedo, longo2);
        vooEsc.adicionarRota(grulis);

        gerVoos.adicionar(vooEsc);

        // O toString vai usar o método implementado
        // em VooEscalas, mas reutilizando (reuso) o método
        // original de Voo
        System.out.println(vooEsc.toString());

//        gerVoos.ordenarDataHoraDuracao();
        gerVoos.ordenarDataHoraDuracao();
        System.out.println("Todos os vôos:\n");
        for(Voo v: gerVoos.listarTodos())
        {
            if(v instanceof VooEscalas) {
                System.out.println(">>> Vôo com escalas!");
                VooEscalas vaux = (VooEscalas) v;
                System.out.println("Escalas: "+vaux.getTotalRotas());
            }
            System.out.println(v);
        }

        // Tarefa 1: listar os vôos de determinada origem

        System.out.println("\nVôos cuja origem é Guarulhos (gru)\n");
        for(Voo v: gerVoos.buscarOrigem("GRU"))
            System.out.println(v);

        // Tarefa 2: mostrar a localização dos aeroportos que operam em determinado período do dia

        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fim    = LocalTime.of(9, 0);

        System.out.println("\nVôos que ocorrem entre 7h e 9h\n");
        for(Voo v: gerVoos.buscarPeriodo(inicio, fim)) {
//            System.out.println(v);
            Aeroporto origem = v.getRota().getOrigem();
            System.out.println(origem.getNome() + ": " +origem.getLocal());
        }

        LocalTime inicio2 = LocalTime.of(9, 0);
        LocalTime fim2    = LocalTime.of(16, 0);

        System.out.println("\nVôos que ocorrem entre 9h e 16h\n");
        for(Voo v: gerVoos.buscarPeriodo(inicio2, fim2)) {
//            System.out.println(v);
            Aeroporto origem = v.getRota().getOrigem();
            System.out.println(origem.getNome() + ": " + origem.getLocal());
        }*/
    }
}
