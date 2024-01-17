package org.example.view;

import org.example.Locations;
import org.example.database.MySQLConnection;
import org.example.fileReader.CSVReader;
import org.example.fileReader.PropertyReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class MainController {

    private MainView mainView;
    private Properties properties;
    private MySQLConnection mySQLConnection;

    public MainController(MainView mainView) {
        this.mainView = mainView;


        mainView.explorer(MainController.this::explorerButton);
        mainView.standard(MainController.this::standardButton);
        mainView.speichern(MainController.this::speicherButton);
        mainView.löschen(MainController.this::löschButton);
        properties = PropertyReader.readProperties();
        mySQLConnection = new MySQLConnection(properties);
        CSVReader.readCsv();
        System.out.println(mySQLConnection.getDbName());




    }

    private void löschButton(ActionEvent actionEvent) {
        mySQLConnection.deleteDB();
    }

    private void speicherButton(ActionEvent actionEvent) {
        mySQLConnection.createDB();
        mySQLConnection.createTables();
        mySQLConnection.addCountries(CSVReader.getListLocation());
        mySQLConnection.addCities(CSVReader.getListLocation());

    }

    private void standardButton(ActionEvent actionEvent) {
        CSVReader.setPath("src/main/resources/country-list.csv");
    }

    private void explorerButton(ActionEvent actionEvent) {
     Path path = getOutputPath(null);
     try {
         CSVReader.setPath(path.toString());
         System.out.println(path.toString());

     }
     catch (NullPointerException e){
         System.out.println(e.getMessage());
     }


    }
    public Path getOutputPath(String s) {
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
