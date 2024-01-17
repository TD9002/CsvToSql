package org.example.database;

import org.example.Locations;
import org.example.fileReader.PropertyReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MySQLConnection implements IDatabaseConnection {
    private  String host, username, password, dbName, tableCity, tableCountry, columnCountry;
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

    public Connection getConnectionIfDatabaseExists() {

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println("Error: Cannot connect to the database. 1" + ex.getMessage());
        }
        return connection;
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

    public boolean addCities(List<Locations> locations){
            String country = null;
            try {
                Statement statement = connection.createStatement();

                for (int i = 0; i < locations.size(); i++) {

                    String querry = "SELECT country from countries WHERE country =" + "\"" +
                            locations.get(i).getCountry() + "\"";

                    ResultSet resultSet = statement.executeQuery(querry);

                    if (resultSet.next()) {
                        country = resultSet.getString("country");
                    }

                    if (!Objects.equals(country, locations.get(i).getCountry())) {
                        querry = "Insert into " + tableCountry +
                                "(" +
                                columnCountry + ") values (" + "\"" +
                                locations.get(i).getCountry() + "\");";

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
    public boolean addCountries(List<Locations> locations) {

        ResultSet resultSet = null;
        int id = 99999999;
        String city = null;
        int countryForeignID = 888888;

        try {
            Statement statement = connection.createStatement();

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
        return false;
    }


}
