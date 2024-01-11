package org.example.database;

import org.example.Locations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySQLConnection implements IDatabaseConnection {
    private  String host = "127.0.0.1";
    private  String username = "root";
    private  String password = "";
    private String tbl_country = "countries", tbl_city = "cities";
    private String column_country = "country",column_city = "city",column_Idcountry = "ID_COUNTRY";

    private  int port = 3306;
    private  String dbname = "csv_java";
    private Connection connection = null;
    private boolean statusCheck;
    private List<Integer> idCountry;


/*    public MySQLConnection(Properties dbProperties) {
        if (dbProperties != null){
            host = dbProperties.getProperty("db.host");
            username = dbProperties.getProperty("db.username");
            dbname = dbProperties.getProperty("db.dbname");
            password ="";
            port = Integer.valueOf(dbProperties.getProperty("db.port"));
        }
    }*/

    public boolean checkForDatabase() {
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
            if (Objects.equals(databaseNames.get(i), dbname)) {
                return true;
            }
        }
        statusCheck = false;
        return false;
        }


    public void createDB(){

        if (!checkForDatabase()){
        try {
            Statement stmt = getConnection().createStatement();

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
       if (statusCheck){
        try {
            Statement stmt = getConnection().createStatement();


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
       else System.out.println("Tabelle existiert bereits");
    }

    //connection wenn db noch nicht existiert
    @Override
    public Connection getConnection() {
        if (this.connection == null && !statusCheck) {
            String url = "jdbc:mysql://" + host + ":" + port + "/";
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Error: Cannot connect to the database. 1" + ex.getMessage());
            }
        }
        else if (this.connection != null && statusCheck) {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Error: Cannot connect to the database. 2" + ex.getMessage());
            }
        }

        return connection;
    }

    @Override
    public boolean csvToSql(List<Locations> locations) {
    return false;
    }

    @Override
    public boolean insertCountries(List<Locations> locations) {
        String country = null;
    try {
    Statement statement = getConnection2().createStatement();

    for (int i = 0; i <locations.size() ; i++) {

        String querry = "SELECT country from countries WHERE country =" + "\"" +
                locations.get(i).getCountry() + "\"";

       ResultSet resultSet = statement.executeQuery(querry);

       if (resultSet.next()){
           country = resultSet.getString("country");
        }

        if (!Objects.equals(country, locations.get(i).getCountry())) {
            querry = "Insert into " + tbl_country +
                    "(" +
                    column_country + ") values (" + "\"" +
                    locations.get(i).getCountry() + "\");";

            statement.executeUpdate(querry);
            }
              }
        statement.close();
        return true;

    }
    catch (SQLException e){
    System.out.println("Länder konnten nicht hinzugefügt werden " +e.getMessage() );
}
        System.out.println("Länder bereits gespeichert");
        return false;
    }

    @Override
    public boolean insertCities(List<Locations> locations) {
        ResultSet resultSet = null;
        int id = 99999999;
        String city = null;
        int countryForeignID = 888888;

        try {
            Statement statement = getConnection2().createStatement();

            for (int i = 0; i <locations.size() ; i++) {

                //lese id des landes für betreffende Stadt aus

                String querry = "SELECT ID_COUNTRY,country from countries WHERE country =" + "\"" +
                        locations.get(i).getCountry() +"\"";

                resultSet = statement.executeQuery(querry);
                if (resultSet.next()){
                    id = resultSet.getInt(column_Idcountry);
                }

                // suche ob stadt bereits existiert
                querry = "SELECT city, ID_COUNTRY from cities WHERE city =" + "\"" +
                        locations.get(i).getCity() +"\"";

                resultSet = statement.executeQuery(querry);

                if (resultSet.next()){
                    city = resultSet.getString("city");
                    countryForeignID = resultSet.getInt((column_Idcountry));

                }


                // speichere stadt ein wenn nicht vorhanden, oder bei gleichem namen aber in einem anderen land
                // @todo im moment gibt es keine begrenzung für städte mit gleichen namen aber unterschiedlichen Land
                if (!Objects.equals(city, locations.get(i).getCity()) || (Objects.equals(city,locations.get(i).getCity()) && countryForeignID != id))
                {
                    querry = "Insert into " + tbl_city +
                            "(" +
                            column_city +"," + column_Idcountry + ") values (" + "\"" +
                            locations.get(i).getCity() + "\", " +
                            id +
                            ");";
                    System.out.println(querry);

                    statement.executeUpdate(querry);
                }
            }
            statement.close();
            System.out.println("Stätdte wurden gespeichert.");
            return true;
        }
        catch (SQLException e){
            System.out.println("Daten konnten nicht hinzugefügt werden. " + e.getMessage());
            return false;
        }
    }

// Connection wenn db erstellt ist
    public Connection getConnection2() {
        if (this.connection != null) {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Error: Cannot connect to the database. 1" + ex.getMessage());
            }
        }

        return connection;
    }

    public void deleteDB(){
        try {
            Statement statement = getConnection2().createStatement();

            String querry = "DROP DATABASE " + dbname;

            statement.executeUpdate(querry);
            System.out.println(dbname + " wurde erfolgreich gelöscht");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
