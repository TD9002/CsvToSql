package org.example.database;

import org.example.Locations;

import java.sql.Connection;
import java.util.List;

public interface IDatabaseConnection {
    Connection getConnection();
    Connection getConnectionIfDatabaseExists();
    void createDB();
    void createTables();
    boolean addCities(List<Locations> locations);
    boolean addCountries(List<Locations> locations);
    void deleteDB();







}
