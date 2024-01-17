
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


}

