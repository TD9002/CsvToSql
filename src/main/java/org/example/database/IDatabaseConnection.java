package org.example.database;

import org.example.Locations;

import java.sql.Connection;
import java.util.List;

public interface IDatabaseConnection {
    Connection getConnection();
    boolean addCities(List<Locations> locations);
    boolean addCountries(List<Locations> locations);







}
