package org.example.database;

import org.example.Locations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MySQLConnection implements IDatabaseConnection {
    private  String host, username, password, dbName;
    private final String tableCity ="tbl_city", tableCountry = "tbl_country",
            columnCountry = "country", columnIdCountry = "idCountry", columnIdCity = "idCity", columnCity = "city";
    private  int port;
    private Connection connection = null;

   public MySQLConnection(Properties properties) {
        if (properties != null){
            host = properties.getProperty("host");
            username = properties.getProperty("username");
            dbName = properties.getProperty("dbName");
            password =properties.getProperty("password");
            port = Integer.valueOf(properties.getProperty("port"));
        }
    }

    //connection wenn db noch nicht existiert
    @Override
    public Connection getConnection() {
        if (this.connection == null) {
            String url = "jdbc:mysql://" + host + ":" + port + "/";
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Error: Cannot connect to the database. " + ex.getMessage());
            }
        }
        return connection;
    }

    //connection wenn DB existiert
    public Connection getConnectionIfDatabaseExists() {

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println("Error: Cannot connect to the database. 1" + ex.getMessage());
        }
        return connection;
    }

    public void createDB(){

                    try {
                System.out.println(dbName);
                Statement stmt = getConnection().createStatement();

                String createDatabase = "CREATE DATABASE IF NOT EXISTS " + dbName; //creates database
                stmt.executeUpdate(createDatabase);

                connection.close();
                stmt.close();
                System.out.println("<< Database created successfully >>");
            }
            catch (SQLException exception){
                System.out.println("Konnte DB nicht anlegen " + exception.getMessage());
            }
   }

    public void createTables(){
        try {
            Statement stmt = getConnectionIfDatabaseExists().createStatement();


            String createTableCountries =  "CREATE TABLE " +
                    tableCountry +"( " +
                    columnIdCountry + " int NOT NULL AUTO_INCREMENT, " +
                    columnCountry + " varchar(255) NOT NULL, PRIMARY KEY (" + columnIdCountry + "))";


            String createTableCities = "CREATE TABLE " + tableCity + "( " +
                    columnIdCity + " int NOT NULL AUTO_INCREMENT, " +
                    columnCity + " varchar(255) NOT NULL," +
                    columnIdCountry + " int NOT NULL," +
                    "PRIMARY KEY (" +
                    columnIdCity + ")," +
                    "FOREIGN KEY (" +
                    columnIdCountry +
                    ") REFERENCES " + tableCountry +
                    "(" + columnIdCountry + "))";




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

    public boolean databaseCheck(){
        List<String> databaseNames = new ArrayList<>();
        try {
            ResultSet resultSet = getConnection().getMetaData().getCatalogs();
            while (resultSet.next()) {
                databaseNames.add(resultSet.getString(1));
            }

            resultSet.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        for (int i = 0; i <databaseNames.size() ; i++) {
            if (Objects.equals(databaseNames.get(i), dbName)) {
                return true;
            }
        }
        return false;
    }

    public boolean addCountries(List<Locations> locations){
            String country = null;
            try {
                Statement statement = getConnectionIfDatabaseExists().createStatement();

                for (int i = 0; i < locations.size(); i++) {

                    String querry = "SELECT " +
                            columnCountry + " from " +
                            tableCountry + " WHERE " +
                            columnCountry + " =" + "\"" +
                            locations.get(i).getCountry() + "\"";

                    ResultSet resultSet = statement.executeQuery(querry);

                    if (resultSet.next()) {
                        country = resultSet.getString(columnCountry);
                    }

                    if (!Objects.equals(country, locations.get(i).getCountry())) {
                        querry = "Insert into " + tableCountry +
                                "(" +
                                columnCountry + ") values (" + "\"" +
                                locations.get(i).getCountry() + "\");";

                        System.out.println(querry);
                        statement.executeUpdate(querry);
                    }
                }
                statement.close();
                return true;

            } catch (SQLException e) {
                System.out.println("Länder konnten nicht hinzugefügt werden " + e.getMessage());
                return false;
            }
        }

    @Override
    public boolean addCities(List<Locations> locations) {

        ResultSet resultSet = null;
        int id = 99999999;
        String city = null;
        int countryForeignID = 888888;

        try {
            Statement statement = getConnectionIfDatabaseExists().createStatement();

            for (int i = 0; i <locations.size() ; i++) {

                //lese id des landes für betreffende Stadt aus

                String querry = "SELECT " + columnIdCountry + "," +
                        columnCountry + " from " +
                        tableCountry + " WHERE " +
                        columnCountry + " =" + "\"" +
                        locations.get(i).getCountry() +"\"";

                resultSet = statement.executeQuery(querry);
                if (resultSet.next()){
                    id = resultSet.getInt(columnIdCountry);
                }

                // suche ob stadt bereits existiert
                querry = "SELECT " + columnCity + "," +
                        columnIdCountry + " from " +
                        tableCity + " WHERE " +
                        columnCity + "= \"" +
                        locations.get(i).getCity() +"\"";


                resultSet = statement.executeQuery(querry);

                if (resultSet.next()){
                    city = resultSet.getString(columnCity);
                    countryForeignID = resultSet.getInt((columnIdCountry));

                }

                // speichere stadt ein wenn nicht vorhanden, oder bei gleichem namen aber in einem anderen land
                // @todo im moment gibt es keine begrenzung für städte mit gleichen namen aber unterschiedlichen Land
                if (!Objects.equals(city, locations.get(i).getCity()) || (Objects.equals(city,locations.get(i).getCity()) && countryForeignID != id))
                {
                    querry = "Insert into " + tableCity +
                            "(" +
                            columnCity +"," + columnIdCountry + ") values (" + "\"" +
                            locations.get(i).getCity() + "\", " +
                            id +
                            ");";

                    statement.executeUpdate(querry);
                    System.out.println(querry);
                }
            }
            statement.close();
            System.out.println("Städte wurden gespeichert.");
            return true;
        }
        catch (SQLException e){
            System.out.println("Städte konnten nicht hinzugefügt werden. " + e.getMessage());
            return false;
        }
    }

    public void deleteDB(){
        try {
            Statement statement = getConnectionIfDatabaseExists().createStatement();

            String querry = "DROP DATABASE " + dbName;

            statement.executeUpdate(querry);
            System.out.println(dbName + " wurde erfolgreich gelöscht");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
   }
