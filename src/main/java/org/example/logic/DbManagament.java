
package org.example.logic;

import org.example.Locations;
import org.example.database.IDatabaseConnection;
import org.example.database.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class DbManagament {
    private IDatabaseConnection connection;
    private String dbName;

    public DbManagament(Properties properties){
        this.connection = new MySQLConnection(properties);
        dbName = properties.getProperty("dbName");

    }

    public void createDB(){

        if (!checkForDatabase()){
            try {
                Statement stmt = connection.getConnection().createStatement();

                String createDatabase = "CREATE DATABASE IF NOT EXISTS " + dbname; //creates database
                stmt.executeUpdate(createDatabase);

                connection.close();
                stmt.close();
                statusCheck =true;
                System.out.println("<< Database created successfully >>");
            }
            catch (SQLException exception){
                System.out.println("Konnte DB nicht anlegen " + exception.getMessage());
            }

        }
        else System.out.println("Database already exists.");
    }

    public void createTables(){
        try {
            Statement stmt = connection.createStatement();


            String createTableCountries =  "CREATE TABLE countries ( " +
                    "ID_COUNTRY int NOT NULL AUTO_INCREMENT, " +
                    "country varchar(255) NOT NULL," +
                    "PRIMARY KEY (ID_COUNTRY))";

            String createTableCities = "CREATE TABLE cities ( " +
                    "ID_CITY int NOT NULL AUTO_INCREMENT, " +
                    "city varchar(255) NOT NULL," +
                    "ID_COUNTRY int NOT NULL," +
                    "PRIMARY KEY (ID_CITY)," +
                    "FOREIGN KEY (ID_COUNTRY) REFERENCES countries(ID_COUNTRY))";


            stmt.executeUpdate(createTableCountries);
            stmt.executeUpdate(createTableCities);

            connection.close();
            stmt.close();

            System.out.println("Tabellen angelegt");
        }
        catch (SQLException exception){
            System.out.println("Konnte Tabellen nicht anlegen " + exception.getMessage());
        }
    }

    public boolean insertCountries(List<Locations> locations) {
        if (locations != null) {
            connection.addCities(locations);
            return true;
        }
            return false;
    }


    public boolean insertCities(List<Locations> locations) {
        if (locations != null) {
            connection.addCities(locations);
            return true;
        }
        return false;

    }




    public void deleteDB(){
        try {
            Statement statement = connection.createStatement();

            String querry = "DROP DATABASE " + dbname;

            statement.executeUpdate(querry);
            System.out.println(dbname + " wurde erfolgreich gelöscht");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

