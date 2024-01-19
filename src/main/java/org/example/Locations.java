package org.example;

public class Locations {
    private String city, country;

    public Locations(String city, String country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
