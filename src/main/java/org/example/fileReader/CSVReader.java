package org.example.fileReader;

import org.example.Locations;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private static  String path = "src/main/resources/country-list.csv";
    private static List<Locations> listLocation;

    public static List<Locations> getListLocation() {
        return listLocation;
    }

    public static void setPath(String path) {
        CSVReader.path = path;
        System.out.println("Pfard=" +path);
    }

    public static void readCsv(){

        listLocation = new ArrayList<>();

        try {
            BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
            String line = bufferedReader.readLine();
            while (line != null){
                String[] attributes = line.split(",");
                String country = attributes[0].replace("\"","");
                String city = attributes[1].replace("\"","");
                listLocation.add(new Locations(city,country));

                line = bufferedReader.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
