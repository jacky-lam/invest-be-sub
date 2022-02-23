package com.lib.database.entity.region;

import com.lib.database.entity.region.model.CountryISO;
import com.lib.database.wrapper.InvestmentDatabaseWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper functions here
 */
public class RegionHelper {

    private final static Logger logger = LogManager.getLogger(RegionHelper.class);

    public enum RegionEnum { // enums are static by default
        north_america,
        united_kingdom,
        europe_developed,
        europe_emerging,
        africa_middle_east,
        japan,
        australasia,
        asia_developed,
        asia_emerging,
        latin_america,
        not_classified
    }

    public static List<CountryISO> countryIsoCache = null; // not thread-safe
    public static List<CountryISO> getCountryISO(InvestmentDatabaseWrapper dbWrapper, boolean refresh) throws SQLException {
        String prefixLog = "getCountryIso - ";

        if(countryIsoCache != null && refresh == false)
            return countryIsoCache;

        // TODO: get data from db
        List<CountryISO> result = new ArrayList<>();
        result.add(new CountryISO("US", "America")); //United States
        result.add(new CountryISO("CA", "America")); //Canada
        result.add(new CountryISO("CR", "America")); //Costa Rica
        result.add(new CountryISO("GB", "United Kingdom")); //Great Britain
        result.add(new CountryISO("DE", "Europe")); //Germany
        result.add(new CountryISO("FR", "Europe")); //France
        result.add(new CountryISO("IE", "Europe")); //Ireland
        result.add(new CountryISO("GR", "Europe")); //Greece
        result.add(new CountryISO("PL", "Europe")); //Poland
        result.add(new CountryISO("IR", "Middle East")); //Iran
        result.add(new CountryISO("AU", "Australasia")); //Australia
        result.add(new CountryISO("CK", "Australasia")); //New Zealand
        result.add(new CountryISO("JP", "Asia")); //Japan
        result.add(new CountryISO("CN", "Asia")); //China
        result.add(new CountryISO("SG", "Asia")); //Singapore
        result.add(new CountryISO("IO", "Asia")); //India
        countryIsoCache = result;

        return countryIsoCache;
    }

}
