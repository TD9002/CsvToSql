package org.example.view;

import org.example.database.MySQLConnection;
import org.example.fileReader.CSVReader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainController {

    private MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;


        mainView.explorer(MainController.this::explorerButton);
        mainView.standard(MainController.this::standardButton);
        mainView.speichern(MainController.this::speicherButton);
        mainView.speichern(MainController.this::löschButton);

    }

    private void löschButton(ActionEvent actionEvent) {
        MySQLConnection mySQLConnection = new MySQLConnection();
        mySQLConnection.createDB();
        mySQLConnection.createTables();
        mySQLConnection.deleteDB();
    }

    private void speicherButton(ActionEvent actionEvent) {
        CSVReader.readCsv();
        MySQLConnection mySQLConnection = new MySQLConnection();
        mySQLConnection.createDB();
        mySQLConnection.createTables();
        mySQLConnection.insertCountries(CSVReader.getListLocation());
        mySQLConnection.insertCities(CSVReader.getListLocation());

    }

    private void standardButton(ActionEvent actionEvent) {
        CSVReader.setPath("src/main/resources/country-list.csv");
    }

    private void explorerButton(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        // Erzeugung eines neuen Frames mit dem Titel "Dateiauswahl"
        JFrame meinJFrame = new JFrame("Dateiauswahl");
        // Wir setzen die Breite auf 450 und die Höhe 300 pixel
        meinJFrame.setSize(450,300);
        // Hole den ContentPane und füge diesem unseren JFileChooser hinzu
        meinJFrame.getContentPane().add(chooser);
        // Wir lassen unseren Frame anzeigen
        meinJFrame.setVisible(true);
    }

    public static void main(String[] args) {
    new MainController(new MainView());
    }
}
