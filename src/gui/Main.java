//Gabriel Silverio - 20106468

package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main extends Application {
    final SwingNode mapkit = new SwingNode();

    private GerenciadorCias gerCias;
    private GerenciadorAeroportos gerAero;
    private GerenciadorRotas gerRotas;
    private GerenciadorAeronaves gerAvioes;
    private GerenciadorPais gerPais;

    private GerenciadorMapa gerenciador;

    private EventosMouse mouse;

    @Override
    public void start(Stage primaryStage) throws Exception {

        setup();

        GeoPosition poa = new GeoPosition(-30.05, -51.18);
        gerenciador = new GerenciadorMapa(poa, GerenciadorMapa.FonteImagens.VirtualEarth);
        mouse = new EventosMouse();
        gerenciador.getMapKit().getMainMap().addMouseListener(mouse);
        gerenciador.getMapKit().getMainMap().addMouseMotionListener(mouse);

        createSwingContent(mapkit);

        BorderPane pane = new BorderPane();
        GridPane leftPane = new GridPane();

        leftPane.setAlignment(Pos.BASELINE_LEFT);
        leftPane.setHgap(10);
        leftPane.setVgap(10);
        leftPane.setPadding(new Insets(10, 10, 10, 10));



        //Text tfTitulo = new Text("Informe o código da Cia. Aerea:");
        Label lCodCia = new Label("Cia. Aerea:");
        Label lCodCia2 = new Label("Cia. Aerea:");
        Label lCodApp = new Label("Partida:");
        Label lCodApc = new Label("Chegada:");
        Label lCodApe = new Label("Escala:");
        Label lItem1 = new Label("Item 1");
        Label lItem2 = new Label("Item 2");
        Label lItem3 = new Label("Item 3");
        Label lItem4 = new Label("Item 4");
        Label lItem5 = new Label("Item 5");
        Label lItem6 = new Label("Item 6");
        Label lCodAp = new Label("Partida: ");
        Label lHora = new Label("Tempo: ");
        TextField txtHora = new TextField();
        Button btnConsulta1 = new Button("Buscar");
        Button btnBuscar2 = new Button("Buscar");
        Button btnBuscar3 = new Button("Buscar");
        Button btnBuscar4 = new Button("Buscar");
        Button btnBuscar5 = new Button("Buscar");
        TextField txtCodCia = new TextField();
        TextField txtCodCia2 = new TextField();
        TextField txtCodAp = new TextField();
        TextField txtCodApp = new TextField();
        TextField txtCodApc = new TextField();
        TextField txtCodApe = new TextField();
        List<Integer> numConexao = new ArrayList<>();
        List<Integer> numConexao2 = new ArrayList<>();
        numConexao.add(0);
        numConexao.add(1);
        numConexao.add(2);
        numConexao2.add(0);
        numConexao2.add(1);
        numConexao2.add(2);
        numConexao2.add(3);
        ComboBox cbNumConexoes = new ComboBox(FXCollections.observableList(numConexao));
        ComboBox cbNumConexoes2 = new ComboBox(FXCollections.observableList(numConexao2));
        ComboBox cbNumRotas;
        ComboBox cbNumRotas2;
        ComboBox cbPaisOrigem = new ComboBox(gerRotas.cbPais(txtCodCia.getText()));
        ComboBox cbPaisDestino = new ComboBox(gerRotas.cbPais(txtCodCia.getText()));
        ComboBox cbAp = new ComboBox();



        //Item 1
//        Desenhar todos os aeroportos onde uma determinada companhia aérea opera. Deve ser
//        possível filtrar a visualização das rotas por país de partida ou chegada.
        leftPane.add(lItem1, 0, 0);
        leftPane.add(lCodCia, 0, 1);
        leftPane.add(txtCodCia, 1, 1);
        //Após o usuario digitar o id da CiaAerea o app faz a busca por todas as rotas feitas pela CiaAerea
        //após o usuario informa o pais de partida e o pais de chegada(atraves do comboBox), assim o app filtra as rotas
        txtCodCia.textProperty().addListener((observable, oldValue, newValue)-> {
            item1(txtCodCia.getText(), null, null);
            cbPaisOrigem.setItems(gerRotas.cbPais(txtCodCia.getText()));
            cbPaisOrigem.setOnAction(e->{
                cbPaisDestino.setItems(gerRotas.cbPais(txtCodCia.getText()));
                cbPaisDestino.setOnAction(i->{
                    item1(txtCodCia.getText(),
                            cbPaisOrigem.getSelectionModel().getSelectedItem().toString().substring(0,2),
                            cbPaisDestino.getSelectionModel().getSelectedItem().toString().substring(0,2));
                });
            });
        });
        leftPane.add(cbPaisOrigem, 0, 2);
        leftPane.add(cbPaisDestino, 1, 2);

        //Item 2
        leftPane.add(lItem2, 0, 4);
        leftPane.add(lCodCia2, 0, 5);
        leftPane.add(txtCodCia2, 1, 5);
        leftPane.add(btnBuscar2, 0, 6);

        //Item 3
        leftPane.add(lItem3, 0, 8);
        leftPane.add(lCodApp, 0, 9);
        leftPane.add(lCodApc, 0, 10);
        leftPane.add(txtCodApp, 1, 9);
        leftPane.add(txtCodApc, 1, 10);
        cbNumRotas = new ComboBox();
        leftPane.add(cbNumConexoes, 0, 11);
        leftPane.add(cbNumRotas, 1, 11);
        leftPane.add(btnBuscar3, 0, 12);

        TextField txtCodApp2 = new TextField();
        TextField txtCodApc2 = new TextField();
        Label lCodApp2 = new Label("Partida: ");
        Label lCodApc2 = new Label("Chegada: ");
        //Item 4
        leftPane.add(lItem4, 0, 14);
        leftPane.add(lCodApp2, 0, 15);
        leftPane.add(txtCodApp2, 1, 15);
        leftPane.add(lCodApe, 0, 16);
        leftPane.add(txtCodApe, 1, 16);
        leftPane.add(lCodApc2, 0, 17);
        leftPane.add(txtCodApc2, 1, 17);

        cbNumRotas2 = new ComboBox();
        leftPane.add(cbNumConexoes2, 0, 18);
        leftPane.add(cbNumRotas2, 1, 18);
        leftPane.add(btnBuscar4, 0, 19);


        //Item 5
        leftPane.add(lItem5, 0, 21);
        leftPane.add(lCodAp, 0, 22);
        leftPane.add(txtCodAp, 1, 22);
        leftPane.add(lHora, 0, 23);
        leftPane.add(txtHora, 1, 23);
        leftPane.add(btnBuscar5, 0, 24);

        //Item 6
//        Label lCidade = new Label("Cidade: ");
//        TextField txtCidade = new TextField();
//        Button btnAdicionarCdd = new Button("Adicionar destino");
//        Button btnBuscar6 = new Button("Gerar rota");
//        leftPane.add(lItem6, 0, 26);
//        leftPane.add(lCidade, 0, 27);
//        leftPane.add(txtCidade, 1, 27);
//        leftPane.add(btnAdicionarCdd, 0, 28);
//        leftPane.add(cbAp, 1, 28);
//        leftPane.add(btnBuscar6, 0, 29);

        //Mostra um "mapa de calor" dos aeroportos mais acessados pela CiaAerea X.
        btnBuscar2.setOnAction(e -> {
            item2(txtCodCia2.getText());
        });

        btnBuscar3.setOnAction(e -> {
            cbNumRotas.setItems(gerRotas.cbListaRota(txtCodApp.getText(), txtCodApc.getText()));
            cbNumRotas.setOnAction(i -> {
                RotaEscala rotaSelected = (RotaEscala) cbNumRotas.getSelectionModel().getSelectedItem();
                destacaRota(rotaSelected);
            });
            cbNumConexoes.setOnAction(j -> {
                int a = (Integer)cbNumConexoes.getSelectionModel().getSelectedItem();
                conexoes(txtCodApp.getText(), txtCodApc.getText(), a);
            });
            item3(txtCodApp.getText(), txtCodApc.getText());
        });

        btnBuscar4.setOnAction(e -> {
            cbNumRotas2.setItems(gerRotas.listaRotaEscala(txtCodApp2.getText(), txtCodApe.getText(), txtCodApc2.getText()));
            cbNumRotas2.setOnAction(i -> {
                RotaEscala rotaSelected = (RotaEscala) cbNumRotas2.getSelectionModel().getSelectedItem();
                destacaRota(rotaSelected);
            });
            cbNumConexoes2.setOnAction(j -> {
                int a = (Integer)cbNumConexoes2.getSelectionModel().getSelectedItem();
                conexoes2(txtCodApp2.getText(), txtCodApe.getText(), txtCodApc2.getText(), a);
            });
            item4(txtCodApp2.getText(), txtCodApe.getText(), txtCodApc2.getText());
        });

        btnBuscar5.setOnAction(e->{
            item5(txtCodAp.getText(),Double.parseDouble(txtHora.getText()));
        });

        pane.setCenter(mapkit);
        pane.setLeft(leftPane);

        Scene scene = new Scene(pane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mapas com JavaFX");
        primaryStage.show();
    }

    // Inicializando os dados aqui...
    private void setup() {
        gerCias = GerenciadorCias.getInstance();
        gerAero = GerenciadorAeroportos.getInstance();
        gerRotas = GerenciadorRotas.getInstance();
        gerAvioes = GerenciadorAeronaves.getInstance();
        gerPais = GerenciadorPais.getInstance();
        try {
            gerAero.carregaDados("airports.dat");
            gerCias.carregaDados("airlines.dat");
            gerRotas.carregaDados("routes.dat");
            gerAvioes.carregaDados("equipment.dat");
            gerPais.carregaDados("countries.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void item1(String codCia, String paisO, String paisD){
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        List<Rota> lstRota = gerRotas.tracadoRota(codCia);
        //Desenha o tracado entre os aeroportos de partida e chegada de acordo com a selecao do usuario
        //e os pontos dos aeroportos
        for(Rota ap:lstRota) {
            if (ap.getOrigem().getPais().getCodigo().equalsIgnoreCase(paisO) && ap.getDestino().getPais().getCodigo().equalsIgnoreCase(paisD)) {
                Tracado tr = new Tracado();
                tr.setLabel(ap.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(0, 255, 0, 60));
                tr.addPonto(ap.getOrigem().getLocal());
                tr.addPonto(ap.getDestino().getLocal());
                gerenciador.addTracado(tr);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
            }else if(paisO==null || paisD == null){
                Tracado tr = new Tracado();
                tr.setLabel(ap.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(255, 255, 10, 60));
                tr.addPonto(ap.getOrigem().getLocal());
                tr.addPonto(ap.getDestino().getLocal());
                gerenciador.addTracado(tr);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
            }
        }
        // Para obter um ponto clicado no mapa, usar como segue:
        //GeoPosition pos = gerenciador.getPosicao();
        //Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

    //Metodo responsavel pelo "mapa de calor"
    private void item2(String codCia){
        gerenciador.clear();
        List<MyWaypoint> lstPoints = new ArrayList<>();
        HashMap<String, Integer> lstSize = new HashMap<>();
        List<Aeroporto> lstAirport = gerRotas.airportsByAirlinesFrom(codCia);

        //Cria um dicionario que utiliza a String do codigo do aeroporto como chave e tem como conteudo
        //o tamanho do ponto que sera mostrado no mapa

        for(Aeroporto ap: lstAirport){//gerRotas.airportsByAirlinesFrom(codCia)){
            if(lstSize.containsKey(ap.getCodigo())){
                lstSize.put(ap.getCodigo(), (lstSize.get(ap.getCodigo())+10));
            }else{
                lstSize.put(ap.getCodigo(), 10);
            }
        }
        //for que verifica o tamanho do ponto para determinar a cor do ponto no mapa
        for (Aeroporto airport : lstAirport){//gerRotas.airportsByAirlinesFrom(codCia)) {
            Color x;
            if(lstSize.get(airport.getCodigo())<=5){
                x = new Color(64,224,208);//turquesa
            }else if(lstSize.get(airport.getCodigo())<=10){
                x = new Color(0,255,255);//ciano
            }else if(lstSize.get(airport.getCodigo())<=20){
                x = new Color(60,179,113);//verde mar;
            }else if(lstSize.get(airport.getCodigo())<=30){
                x = new Color(154,205,50);//amarelo/verde
            }else if(lstSize.get(airport.getCodigo())<=40){
                x = new Color(255,255,0);//amarelo;
            }else if(lstSize.get(airport.getCodigo())<=50){
                x = new Color(218,165,32);//golden rod;
            }else if(lstSize.get(airport.getCodigo())<=60){
                x = new Color(184,143,11,30);//dark goldenrod;
            }else if(lstSize.get(airport.getCodigo())<=70){
                x = new Color(255,114,86,30);//coral1
            }else{
                lstSize.put(airport.getCodigo(),100);
                x = new Color(139,62,47,30);//coral4
            }
            lstPoints.add(new MyWaypoint(x, airport.getCodigo(), airport.getLocal(), lstSize.get(airport.getCodigo())));
        }
        // Para obter um ponto clicado no mapa, usar como segue:
        //GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        lstAirport.clear();
        lstPoints.clear();
        lstSize.clear();
        gerenciador.getMapKit().repaint();
    }

    private void item3(String apPartida, String apChegada){
        gerenciador.clear();
        List<MyWaypoint> lstPoints = new ArrayList<>();
        List<RotaEscala> lstRota = gerRotas.possiveisRotas(gerAero.buscarCodigo(apPartida),gerAero.buscarCodigo(apChegada));

        for(RotaEscala ap:lstRota){
            Tracado tr2 = new Tracado();
            for(Rota ap1:ap.getRotas()) {
                Tracado tr = new Tracado();
                tr.setLabel(ap1.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(0, 255, 0, 60));
                tr.addPonto(ap1.getOrigem().getLocal());
                tr.addPonto(ap1.getDestino().getLocal());
                gerenciador.addTracado(tr);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getOrigem().getCodigo(), ap1.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getDestino().getCodigo(), ap1.getDestino().getLocal(), 10));
            }
            tr2.setLabel(ap.getOrigem().getCodigo());
            tr2.setWidth(5);
            tr2.setCor(new Color(0, 255, 0, 60));
            tr2.addPonto(ap.getOrigem().getLocal());
            tr2.addPonto(ap.getDestino().getLocal());
            gerenciador.addTracado(tr2);
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
        }
        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

    private void destacaRota(RotaEscala rota){
        gerenciador.clear();
        List<MyWaypoint> lstPoints = new ArrayList<>();
        Tracado tr1 = new Tracado();
        tr1.setLabel(rota.getOrigem().getCodigo());
        tr1.setWidth(5);
        tr1.setCor(new Color(0, 255, 0, 100));
        tr1.addPonto(rota.getOrigem().getLocal());
        tr1.addPonto(rota.getDestino().getLocal());
        lstPoints.add(new MyWaypoint(Color.BLUE, rota.getOrigem().getCodigo(), rota.getOrigem().getLocal(), 10));
        lstPoints.add(new MyWaypoint(Color.BLUE, rota.getDestino().getCodigo(), rota.getDestino().getLocal(), 10));
        gerenciador.addTracado(tr1);
            for(Rota ap1:rota.getRotas()) {
                Tracado tr = new Tracado();
                tr.setLabel(ap1.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(0, 255, 0, 100));
                tr.addPonto(ap1.getOrigem().getLocal());
                tr.addPonto(ap1.getDestino().getLocal());
                gerenciador.addTracado(tr);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getOrigem().getCodigo(), ap1.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getDestino().getCodigo(), ap1.getDestino().getLocal(), 10));
            }

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

    private void conexoes(String apPartida, String apChegada, Integer a){
        gerenciador.clear();
        List<MyWaypoint> lstPoints = new ArrayList<>();
        List<RotaEscala> lstRota = gerRotas.possiveisRotas(gerAero.buscarCodigo(apPartida),gerAero.buscarCodigo(apChegada));

        for(RotaEscala ap:lstRota){
            if(ap.getNumeroRota()==a) {
                Tracado tr2 = new Tracado();
                for (Rota ap1 : ap.getRotas()) {
                    Tracado tr = new Tracado();
                    tr.setLabel(ap1.getOrigem().getCodigo());
                    tr.setWidth(5);
                    tr.setCor(new Color(0, 0, 0, 60));
                    tr.addPonto(ap1.getOrigem().getLocal());
                    tr.addPonto(ap1.getDestino().getLocal());
                    gerenciador.addTracado(tr);
                    lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getOrigem().getCodigo(), ap1.getOrigem().getLocal(), 10));
                    lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getDestino().getCodigo(), ap1.getDestino().getLocal(), 10));
                }
                tr2.setLabel(ap.getOrigem().getCodigo());
                tr2.setWidth(5);
                tr2.setCor(new Color(0, 0, 0, 60));
                tr2.addPonto(ap.getOrigem().getLocal());
                tr2.addPonto(ap.getDestino().getLocal());
                gerenciador.addTracado(tr2);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
            }
        }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
        for (Rota airport : lstRota) {
            lstPoints.add(new MyWaypoint(Color.BLUE, airport.getOrigem().getCodigo(), airport.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.BLUE, airport.getDestino().getCodigo(), airport.getDestino().getLocal(), 10));
        }

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }
    //faz a mesma função do item2, mas desta vez o usuario seleciona 1 aeroporto para fazer uma parada
    private void item4(String apPartida, String apEscala, String apChegada) {

        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        List<RotaEscala> lstRota = gerRotas.possiveisRotasEscala(gerAero.buscarCodigo(apPartida),gerAero.buscarCodigo(apEscala), gerAero.buscarCodigo(apChegada));

        for(RotaEscala ap:lstRota){
            Tracado tr2 = new Tracado();
            for(Rota ap1:ap.getRotas()) {
                Tracado tr = new Tracado();
                tr.setLabel(ap1.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(0, 0, 0, 60));
                tr.addPonto(ap1.getOrigem().getLocal());
                tr.addPonto(ap1.getDestino().getLocal());
                gerenciador.addTracado(tr);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getOrigem().getCodigo(), ap1.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getDestino().getCodigo(), ap1.getDestino().getLocal(), 10));
            }
            tr2.setLabel(ap.getOrigem().getCodigo());
            tr2.setWidth(5);
            tr2.setCor(new Color(0, 0, 0, 60));
            tr2.addPonto(ap.getOrigem().getLocal());
            tr2.addPonto(ap.getDestino().getLocal());
            gerenciador.addTracado(tr2);
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
        }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
//        for (Rota airport : lstRota) {
//            lstPoints.add(new MyWaypoint(Color.RED, airport.getOrigem().getCodigo(), airport.getOrigem().getLocal(), 10));
//            lstPoints.add(new MyWaypoint(Color.RED, airport.getDestino().getCodigo(), airport.getDestino().getLocal(), 10));
//        }

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }
    //Desenha no mapa as rotas de acordo com o numero de conexoes selecionadas pelo usuario
    private void conexoes2(String apPartida, String apEscala, String apChegada, Integer a){
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        List<RotaEscala> lstRota = gerRotas.possiveisRotasEscala(gerAero.buscarCodigo(apPartida),gerAero.buscarCodigo(apEscala),gerAero.buscarCodigo(apChegada));

        for(RotaEscala ap:lstRota){
            if(ap.getNumeroRota()==a) {
                Tracado tr2 = new Tracado();
                for (Rota ap1 : ap.getRotas()) {

                    Tracado tr = new Tracado();
                    tr.setLabel(ap1.getOrigem().getCodigo());
                    tr.setWidth(5);
                    tr.setCor(new Color(0, 0, 0, 60));
                    tr.addPonto(ap1.getOrigem().getLocal());
                    tr.addPonto(ap1.getDestino().getLocal());
                    gerenciador.addTracado(tr);
                    lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getOrigem().getCodigo(), ap1.getOrigem().getLocal(), 10));
                    lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getDestino().getCodigo(), ap1.getDestino().getLocal(), 10));
                }
                tr2.setLabel(ap.getOrigem().getCodigo());
                tr2.setWidth(5);
                tr2.setCor(new Color(0, 0, 0, 60));
                tr2.addPonto(ap.getOrigem().getLocal());
                tr2.addPonto(ap.getDestino().getLocal());
                gerenciador.addTracado(tr2);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
            }
        }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
//        for (Rota airport : lstRota) {
//            lstPoints.add(new MyWaypoint(Color.RED, airport.getOrigem().getCodigo(), airport.getOrigem().getLocal(), 10));
//            lstPoints.add(new MyWaypoint(Color.RED, airport.getDestino().getCodigo(), airport.getDestino().getLocal(), 10));
//        }

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

    private void item5(String apPartida, Double hora){
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        List<RotaEscala> lstRota = gerRotas.possiveisDestino(gerAero.buscarCodigo(apPartida),hora);

        for(RotaEscala ap:lstRota){
            Tracado tr2 = new Tracado();
            for(Rota ap1:ap.getRotas()) {
                Tracado tr = new Tracado();
                tr.setLabel(ap1.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(0, 0, 0, 60));
                tr.addPonto(ap1.getOrigem().getLocal());
                tr.addPonto(ap1.getDestino().getLocal());
                gerenciador.addTracado(tr);
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getOrigem().getCodigo(), ap1.getOrigem().getLocal(), 10));
                lstPoints.add(new MyWaypoint(Color.BLUE, ap1.getDestino().getCodigo(), ap1.getDestino().getLocal(), 10));
            }
            tr2.setLabel(ap.getOrigem().getCodigo());
            tr2.setWidth(5);
            tr2.setCor(new Color(0, 0, 0, 60));
            tr2.addPonto(ap.getOrigem().getLocal());
            tr2.addPonto(ap.getDestino().getLocal());
            gerenciador.addTracado(tr2);
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getOrigem().getCodigo(), ap.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getDestino().getCodigo(), ap.getDestino().getLocal(), 10));
        }
        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

//    private void item6(String apPartida, Double hora){
//        List<MyWaypoint> lstPoints = new ArrayList<>();
//        gerenciador.clear();
//        List<RotaEscala> lstRota = gerRotas.possiveisDestino(gerAero.buscarCodigo(apPartida),hora);
//
//
//
//        // Para obter um ponto clicado no mapa, usar como segue:
//        GeoPosition pos = gerenciador.getPosicao();
//        // Informa o resultado para o gerenciador
//        gerenciador.setPontos(lstPoints);
//        // Quando for o caso de limpar os traçados...
//        //gerenciador.clear();
//        gerenciador.getMapKit().repaint();
//    }



    private class EventosMouse extends MouseAdapter {
        private int lastButton = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerenciador.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
            System.out.println(loc.getLatitude()+", "+loc.getLongitude());
            lastButton = e.getButton();
            // Botão 3: seleciona localização
            if (lastButton == MouseEvent.BUTTON3) {
                gerenciador.setPosicao(loc);
                gerenciador.getMapKit().repaint();
            }
        }
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(gerenciador.getMapKit());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
