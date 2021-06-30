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

    private ObservableList<CiaAerea> comboCiasData;
    private ComboBox<CiaAerea> comboCia;

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
        GridPane leftPane2 = new GridPane();

        leftPane.setAlignment(Pos.BASELINE_LEFT);
        leftPane.setHgap(10);
        leftPane.setVgap(10);
        leftPane.setPadding(new Insets(10, 10, 10, 10));

        leftPane2.setAlignment(Pos.CENTER);
        leftPane2.setHgap(5);
        leftPane2.setVgap(5);
        //leftPane2.setPadding(new Insets(10, 10, 10, 10));


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
        Button btnConsulta1 = new Button("Buscar");
        Button btnBuscar2 = new Button("Buscar");
        Button btnBuscar3 = new Button("Buscar");
        Button btnBuscar4 = new Button("Buscar");
        TextField txtCodCia = new TextField();
        TextField txtCodCia2 = new TextField();
        TextField txtCodApp = new TextField();
        TextField txtCodApc = new TextField();
        TextField txtCodApe = new TextField();
        List<Integer> numConexao = new ArrayList<>();
        numConexao.add(0);
        numConexao.add(1);
        numConexao.add(2);
        ComboBox cbNumConexoes = new ComboBox(FXCollections.observableList(numConexao));
        ComboBox cbNumRotas;
        ComboBox cbPaisOrigem = new ComboBox(gerRotas.listaRota(txtCodCia.getText()));
        ComboBox cbPaisDestino = new ComboBox(gerRotas.listaRota(txtCodCia.getText()));

//        ToggleGroup rbPartCheg = new ToggleGroup();
//        RadioButton rbPartida = new RadioButton("Partida");
//        rbPartida.setToggleGroup(rbPartCheg);
//        RadioButton rbChegada = new RadioButton("Chegada");
//        rbChegada.setToggleGroup(rbPartCheg);
//        rbPartida.setSelected(true);
//        leftPane.add(rbPartida,0,2);
//        leftPane.add(rbChegada,1,2);


        //Item 1
        leftPane.add(lItem1, 0, 0);
        leftPane.add(lCodCia, 0, 1);
        leftPane.add(txtCodCia, 1, 1);
        txtCodCia.textProperty().addListener((observable, oldValue, newValue)-> {
            cbPaisOrigem.setItems(gerRotas.listaRota(txtCodCia.getText()));
            cbPaisOrigem.setOnAction(e->{
                cbPaisDestino.setItems(gerRotas.listaRota(txtCodCia.getText()));
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

        //Item 4
        leftPane.add(lItem4, 0, 14);
        leftPane.add(lCodApe, 0, 15);
        leftPane.add(txtCodApe, 1, 15);
        leftPane.add(btnBuscar4, 0, 16);


        btnBuscar2.setOnAction(e -> {
            item2(txtCodCia2.getText());
        });

        btnBuscar3.setOnAction(e -> {
            cbNumRotas.setItems(gerRotas.listaRotaF(txtCodApp.getText(), txtCodApc.getText()));
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
            consulta4();
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
        List<Rota> lstRota = gerRotas.listaRota2(codCia);

        //mostra o traçado de acordo com o pais de partida e chegada
            for(Rota ap:lstRota){
                if(ap.getOrigem().getPais().getCodigo().equalsIgnoreCase(paisO) && ap.getDestino().getPais().getCodigo().equalsIgnoreCase(paisD)){
                    Tracado tr = new Tracado();
                    tr.setLabel(ap.getOrigem().getCodigo());
                    tr.setWidth(5);
                    tr.setCor(new Color(0,0,0,60));
                    tr.addPonto(ap.getOrigem().getLocal());
                    tr.addPonto(ap.getDestino().getLocal());
                    gerenciador.addTracado(tr);
                }
            }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
            for (Aeroporto airport : gerRotas.airportsByAirlinesFrom(codCia)) {
                lstPoints.add(new MyWaypoint(Color.RED, airport.getCodigo(), airport.getLocal(), 10));
            }

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

    private void item2(String codCia){

        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();

        HashMap<String, Integer> lstSize = new HashMap<>();

        for(Aeroporto ap: gerRotas.airportsByAirlinesFrom(codCia)){
            if(lstSize.containsKey(ap.getCodigo())){
                lstSize.put(ap.getCodigo(), (lstSize.get(ap.getCodigo())+5));
            }else{
                lstSize.put(ap.getCodigo(), 5);
            }
        }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
        Color x;
        int cor, aux;
        for (Aeroporto airport : gerRotas.airportsByAirlinesFrom(codCia)) {
            if(lstSize.get(airport.getCodigo())<=5){
                x = Color.CYAN;
            }else if(lstSize.get(airport.getCodigo())<=10){
                x = Color.GREEN;
            }else if(lstSize.get(airport.getCodigo())<=20){
                x = Color.YELLOW;
            }else if(lstSize.get(airport.getCodigo())<=30){
                x = Color.ORANGE;
            }else if(lstSize.get(airport.getCodigo())<=40){
                x = Color.RED;
            }else if(lstSize.get(airport.getCodigo())<=50){
                x = Color.BLACK;
            }else{
                lstSize.put(airport.getCodigo(), 70);
                x = Color.BLUE;
            }
            //x = new Color(200,100,30,90);
            lstPoints.add(new MyWaypoint(x, airport.getCodigo(), airport.getLocal(), lstSize.get(airport.getCodigo())));
        }
        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }

    private void consulta4() {

        //Lista para armazenar o resultado da consulta
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
//        Tracado tr = new Tracado();
//        tr.setLabel("Teste");
//        tr.setWidth(10);
//        tr.setCor(new Color(0,0,0,60));
//        tr.addPonto(poa.getLocal());
//        tr.addPonto(mia.getLocal());
//
//        gerenciador.addTracado(tr);
//
//        Tracado tr2 = new Tracado();
//        tr2.setWidth(5);
//        tr2.setCor(Color.BLUE);
//        tr2.addPonto(gru.getLocal());
//        tr2.addPonto(lis.getLocal());
//        gerenciador.addTracado(tr2);

        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints

        for(Aeroporto ap: gerAero.listarTodas()) {
            lstPoints.add(new MyWaypoint(Color.BLUE, ap.getCodigo(), ap.getLocal(), 15));
        }
//        lstPoints.add(new MyWaypoint(Color.RED, poa.getCodigo(), poa.getLocal(), 15));
//        lstPoints.add(new MyWaypoint(Color.RED, gru.getCodigo(), gru.getLocal(), 5));
//        lstPoints.add(new MyWaypoint(Color.RED, lis.getCodigo(), lis.getLocal(), 5));
//        lstPoints.add(new MyWaypoint(Color.RED, mia.getCodigo(), mia.getLocal(), 5));

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();

        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);

        // Quando for o caso de limpar os traçados...
        // gerenciador.clear();

        gerenciador.getMapKit().repaint();
    }

    private void item3(String apPartida, String apChegada){

        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        List<RotaEscala> lstRota = gerRotas.possiveisRotas(gerAero.buscarCodigo(apPartida),gerAero.buscarCodigo(apChegada));

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
            }
            tr2.setLabel(ap.getOrigem().getCodigo());
            tr2.setWidth(5);
            tr2.setCor(new Color(0, 0, 0, 60));
            tr2.addPonto(ap.getOrigem().getLocal());
            tr2.addPonto(ap.getDestino().getLocal());
            gerenciador.addTracado(tr2);
        }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
        for (Rota airport : lstRota) {
            lstPoints.add(new MyWaypoint(Color.RED, airport.getOrigem().getCodigo(), airport.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.RED, airport.getDestino().getCodigo(), airport.getDestino().getLocal(), 10));
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
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        Tracado tr1 = new Tracado();
        tr1.setLabel(rota.getOrigem().getCodigo());
        tr1.setWidth(5);
        tr1.setCor(new Color(0, 255, 0, 60));
        tr1.addPonto(rota.getOrigem().getLocal());
        tr1.addPonto(rota.getDestino().getLocal());
        lstPoints.add(new MyWaypoint(Color.CYAN, rota.getOrigem().getCodigo(), rota.getOrigem().getLocal(), 10));
        lstPoints.add(new MyWaypoint(Color.CYAN, rota.getDestino().getCodigo(), rota.getDestino().getLocal(), 10));
        gerenciador.addTracado(tr1);
            for(Rota ap1:rota.getRotas()) {
                Tracado tr = new Tracado();
                tr.setLabel(ap1.getOrigem().getCodigo());
                tr.setWidth(5);
                tr.setCor(new Color(0, 255, 0, 60));
                tr.addPonto(ap1.getOrigem().getLocal());
                tr.addPonto(ap1.getDestino().getLocal());
                gerenciador.addTracado(tr);
            }

        for(Rota ap2: rota.getRotas()) {
            lstPoints.add(new MyWaypoint(Color.CYAN, ap2.getOrigem().getCodigo(), ap2.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.CYAN, ap2.getDestino().getCodigo(), ap2.getDestino().getLocal(), 10));
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
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
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
                }
                tr2.setLabel(ap.getOrigem().getCodigo());
                tr2.setWidth(5);
                tr2.setCor(new Color(0, 0, 0, 60));
                tr2.addPonto(ap.getOrigem().getLocal());
                tr2.addPonto(ap.getDestino().getLocal());
                gerenciador.addTracado(tr2);
            }
        }
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
        for (Rota airport : lstRota) {
            lstPoints.add(new MyWaypoint(Color.RED, airport.getOrigem().getCodigo(), airport.getOrigem().getLocal(), 10));
            lstPoints.add(new MyWaypoint(Color.RED, airport.getDestino().getCodigo(), airport.getDestino().getLocal(), 10));
        }

        // Para obter um ponto clicado no mapa, usar como segue:
        GeoPosition pos = gerenciador.getPosicao();
        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);
        // Quando for o caso de limpar os traçados...
        //gerenciador.clear();
        gerenciador.getMapKit().repaint();
    }


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
