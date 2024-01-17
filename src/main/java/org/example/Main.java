package org.example;

import org.example.database.MySQLConnection;
import org.example.fileReader.CSVReader;
import org.example.fileReader.PropertyReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Main {



    public static void main(String[] args) {

     //   CSVReader.readCsv();
        MySQLConnection mySQLConnection = new MySQLConnection(PropertyReader.readProperties());
        System.out.println(mySQLConnection.getDbName());

       // mySQLConnection.insertCountries(CSVReader.getListLocation());
     //   mySQLConnection.insertCities(CSVReader.getListLocation());
       // System.out.println(CSVReader.getListLocation().get(158).getCity());
      //  mySQLConnection.csvToSql(CSVReader.getListLocation());
      //  mySQLConnection.addCities(CSVReader.getListLocation());










    }


}