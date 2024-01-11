package org.example.view;

import org.example.database.MySQLConnection;
import org.example.fileReader.CSVReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

public class MainController {

    private MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;


        mainView.explorer(MainController.this::explorerButton);
        mainView.standard(MainController.this::standardButton);
        mainView.speichern(MainController.this::speicherButton);
        mainView.löschen(MainController.this::löschButton);

    }

    private void löschButton(ActionEvent actionEvent) {
        MySQLConnection mySQLConnection = new MySQLConnection();
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
     Path path = getOutputPath(null);
     CSVReader.setPath(path.toString());
     System.out.println(path.toString());


    }
    public static Path getOutputPath(String s) {
        JFileChooser jd= s == null ? new JFileChooser() : new JFileChooser(s);
        jd.setDialogTitle("Choose output file");
        int returnVal= jd.showSaveDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) return null;
        return jd.getSelectedFile().toPath();
    }
    public static void main(String[] args) {
    new MainController(new MainView());
    }
}
