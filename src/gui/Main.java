package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

        leftPane.setAlignment(Pos.CENTER);
        leftPane.setHgap(10);
        leftPane.setVgap(10);
        leftPane.setPadding(new Insets(10, 10, 10, 10));

        Button btnConsulta1 = new Button("Consulta 1");
        Button btnConsulta2 = new Button("Consulta 2");
        Button btnConsulta3 = new Button("Consulta 3");
        Button btnConsulta4 = new Button("Consulta 4");

        leftPane.add(btnConsulta1, 0, 0);
        leftPane.add(btnConsulta2, 2, 0);
        leftPane.add(btnConsulta3, 3, 0);
        leftPane.add(btnConsulta4, 4, 0);

        btnConsulta1.setOnAction(e -> {
            consulta1("5Q");
        });

        btnConsulta2.setOnAction(e -> {
            consulta2("5Q");
        });

        btnConsulta4.setOnAction(e -> {
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

    private void consulta1(String cod){
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
        for (Aeroporto airport : gerRotas.airportsByAirlinesFrom(cod)) {
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

    private void consulta2(String cod){
        List<MyWaypoint> lstPoints = new ArrayList<>();
        gerenciador.clear();
        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints
        for (Aeroporto airport : gerRotas.airportsByAirlinesTo(cod)) {
            lstPoints.add(new MyWaypoint(Color.BLUE, airport.getCodigo(), airport.getLocal(), 10));
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
