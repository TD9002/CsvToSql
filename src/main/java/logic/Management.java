package logic;

import org.example.database.IDatabaseConnection;

import java.util.List;

public class Management {
    IDatabaseConnection connection;

    public Management(IDatabaseConnection connection) {
        this.connection = connection;
    }

    public boolean saveCSV(List<String> capitalCities, List<String> country) {
        if (capitalCities == null || country == null) return false;
        return false;
    }





}
