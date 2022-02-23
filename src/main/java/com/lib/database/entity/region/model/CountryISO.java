package com.lib.database.entity.region.model;


public class CountryISO {

    private String countryIsoCode;
    private String countryRegion;

    public CountryISO(String countryIsoCode, String countryRegion) {
        this.countryIsoCode = countryIsoCode;
        this.countryRegion = countryRegion;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    @Override
    public String toString() {
        return "CountryISO{" +
                "countryIsoCode='" + countryIsoCode + '\'' +
                ", countryRegion=" + countryRegion +
                '}';
    }
}
