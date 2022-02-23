package com.lib.database.entity.sector;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper functions here
 */
public class SectorHelper {

    /**
     * Sector Enum mainly used by funds (stocks do also contain them, but we dont use it)
     */
    public enum SectorEnum {
        basic_materials,
        consumer_cyclicals,
        financial_services,
        real_estate,
        communication_services,
        energy,
        industrials,
        technology,
        consumer_defensive,
        healthcare,
        utilities
    }

    /**
     * GICS Enum mainly used by stocks only
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum GicsSectorEnum {
        energy("ENERGY"),
        materials("MATERIALS"),
        industrials("INDUSTRIALS"),
        consumer_discretionary("CONSUMER DISCRETIONARY"),
        consumer_staples("CONSUMER STAPLES"),
        health_care("HEALTH CARE"),
        financials("FINANCIALS"),
        information_technology("INFORMATION TECHNOLOGY"),
        communication_services("COMMUNICATION SERVICES"),
        utilities("UTILITIES"),
        real_estate("REAL ESTATE");

        private static final Map<String, GicsSectorEnum> enumByName = new HashMap<>(values().length, 1);
        static {
            for (GicsSectorEnum c : values()) enumByName.put(c.name, c);
        }
        public static GicsSectorEnum getByName(String name) throws IllegalArgumentException {
            GicsSectorEnum result = enumByName.get(name);
            if (result == null) {
                throw new IllegalArgumentException("Invalid name: " + name);
            }
            return result;
        }

        public final String name;
        public String getCode(){
            return this.name();
        }

        GicsSectorEnum(String name){
            this.name = name;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum GicsIndustryGroupEnum {
        energy("ENERGY", GicsSectorEnum.energy),
        materials("MATERIALS", GicsSectorEnum.materials),
        capital_goods("CAPITAL GOODS", GicsSectorEnum.industrials),
        commercial_and_professional_services("COMMERCIAL & PROFESSIONAL SERVICES", GicsSectorEnum.industrials),
        transportation("TRANSPORTATION", GicsSectorEnum.industrials),
        automobiles_and_components("AUTOMOBILES & COMPONENTS", GicsSectorEnum.consumer_discretionary),
        consumer_durables_and_apparel("CONSUMER DURABLES & APPAREL", GicsSectorEnum.consumer_discretionary),
        consumer_services("CONSUMER SERVICES", GicsSectorEnum.consumer_discretionary),
        retailing("RETAILING", GicsSectorEnum.consumer_discretionary),
        food_and_staples_retailing("FOOD & STAPLES RETAILING", GicsSectorEnum.consumer_staples),
        food_beverage_and_tobacco("FOOD, BEVERAGE & TOBACCO", GicsSectorEnum.consumer_staples),
        household_and_personal_products("HOUSEHOLD & PERSONAL PRODUCTS", GicsSectorEnum.consumer_staples),
        health_care_equipment_and_services("HEALTH CARE EQUIPMENT & SERVICES", GicsSectorEnum.health_care),
        pharmaceuticals_biotechnology_and_life_sciences("PHARMACEUTICALS, BIOTECHNOLOGY & LIFE SCIENCES", GicsSectorEnum.health_care),
        banks("BANKS", GicsSectorEnum.financials),
        diversified_financials("DIVERSIFIED FINANCIALS", GicsSectorEnum.financials),
        insurance("INSURANCE", GicsSectorEnum.financials),
        software_and_services("SOFTWARE & SERVICES", GicsSectorEnum.information_technology),
        technology_hardware_and_equipment("TECHNOLOGY HARDWARE & EQUIPMENT", GicsSectorEnum.information_technology),
        semiconductors_and_semiconductor_equipment("SEMICONDUCTORS & SEMICONDUCTOR EQUIPMENT", GicsSectorEnum.information_technology),
        telecommunication_services("TELECOMMUNICATION SERVICES", GicsSectorEnum.communication_services),
        media_and_entertainment("MEDIA & ENTERTAINMENT", GicsSectorEnum.communication_services),
        utilities("MEDIA & ENTERTAINMENT", GicsSectorEnum.utilities),
        real_estate("MEDIA & ENTERTAINMENT", GicsSectorEnum.real_estate)
        ;

        private static final Map<String, GicsIndustryGroupEnum> enumByName = new HashMap<>(values().length, 1);
        static {
            for (GicsIndustryGroupEnum c : values()) enumByName.put(c.name, c);
        }
        public static GicsIndustryGroupEnum getByName(String name) throws IllegalArgumentException {
            GicsIndustryGroupEnum result = enumByName.get(name);
            if (result == null) {
                throw new IllegalArgumentException("Invalid name: " + name);
            }
            return result;
        }

        public final String name;
        public final GicsSectorEnum gicsSector;

        public String getGicsSector() {
            return gicsSector.name();
        }

        public String getCode(){
            return this.name();
        }

        GicsIndustryGroupEnum(String name, GicsSectorEnum gicsSector){
            this.name = name;
            this.gicsSector = gicsSector;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum GicsIndustryEnum {
        energy_equipment_and_services("ENERGY EQUIPMENT & SERVICES", GicsIndustryGroupEnum.energy),
        oil_gas_and_consumable_fuels("OIL, GAS & CONSUMABLE FUELS", GicsIndustryGroupEnum.energy),
        chemicals("CHEMICALS", GicsIndustryGroupEnum.materials),
        construction_materials("CONSTRUCTION MATERIALS", GicsIndustryGroupEnum.materials),
        containers_and_packaging("CONTAINERS & PACKAGING", GicsIndustryGroupEnum.materials),
        metals_and_mining("METALS & MINING", GicsIndustryGroupEnum.materials),
        paper_and_forest_products("PAPER & FOREST PRODUCTS", GicsIndustryGroupEnum.materials),
        aerospace_and_defense("AEROSPACE & DEFENSE", GicsIndustryGroupEnum.capital_goods),
        building_products("BUILDING PRODUCTS", GicsIndustryGroupEnum.capital_goods),
        construction_and_engineering("CONSTRUCTION & ENGINEERING", GicsIndustryGroupEnum.capital_goods),
        electrical_equipment("ELECTRICAL EQUIPMENT", GicsIndustryGroupEnum.capital_goods),
        industrial_conglomerates("INDUSTRIAL CONGLOMERATES", GicsIndustryGroupEnum.capital_goods),
        machinery("MACHINERY", GicsIndustryGroupEnum.capital_goods),
        trading_companies_and_distributors("TRADING COMPANIES & DISTRIBUTORS", GicsIndustryGroupEnum.capital_goods),
        commercial_services_and_supplies("COMMERCIAL SERVICES & SUPPLIES", GicsIndustryGroupEnum.commercial_and_professional_services),
        professional_services("PROFESSIONAL SERVICES", GicsIndustryGroupEnum.commercial_and_professional_services),
        air_freight_and_logistics("AIR FREIGHT & LOGISTICS", GicsIndustryGroupEnum.transportation),
        airlines("AIRLINES", GicsIndustryGroupEnum.transportation),
        marine("MARINE", GicsIndustryGroupEnum.transportation),
        road_and_rail("ROAD & RAIL", GicsIndustryGroupEnum.transportation),
        transportation_infrastructure("TRANSPORTATION INFRASTRUCTURE", GicsIndustryGroupEnum.transportation),
        auto_components("AUTO COMPONENTS", GicsIndustryGroupEnum.automobiles_and_components),
        automobiles("AUTOMOBILES", GicsIndustryGroupEnum.automobiles_and_components),
        household_durables("HOUSEHOLD DURABLES", GicsIndustryGroupEnum.consumer_durables_and_apparel),
        leisure_products("LEISURE PRODUCTS", GicsIndustryGroupEnum.consumer_durables_and_apparel),
        textiles_apparel_and_luxury_goods("TEXTILES, APPAREL & LUXURY GOODS", GicsIndustryGroupEnum.consumer_durables_and_apparel),
        hotels_restaurants_and_leisure("HOTELS, RESTAURANTS & LEISURE", GicsIndustryGroupEnum.consumer_services),
        diversified_consumer_services("DIVERSIFIED CONSUMER SERVICES", GicsIndustryGroupEnum.consumer_services),
        distributors("DISTRIBUTORS", GicsIndustryGroupEnum.retailing),
        internet_and_direct_marketing_retail("INTERNET & DIRECT MARKETING RETAIL", GicsIndustryGroupEnum.retailing),
        multiline_retail("MULTILINE RETAIL", GicsIndustryGroupEnum.retailing),
        specialty_retail("SPECIALTY RETAIL", GicsIndustryGroupEnum.retailing),
        food_and_staples_retailing("FOOD & STAPLES RETAILING", GicsIndustryGroupEnum.food_and_staples_retailing),
        beverages("BEVERAGES", GicsIndustryGroupEnum.food_beverage_and_tobacco),
        food_products("FOOD PRODUCTS", GicsIndustryGroupEnum.food_beverage_and_tobacco),
        tobacco("TOBACCO", GicsIndustryGroupEnum.food_beverage_and_tobacco),
        household_products("HOUSEHOLD PRODUCTS", GicsIndustryGroupEnum.household_and_personal_products),
        personal_products("PERSONAL PRODUCTS", GicsIndustryGroupEnum.household_and_personal_products),
        health_care_equipment_and_supplies("HEALTH CARE EQUIPMENT & SUPPLIES", GicsIndustryGroupEnum.health_care_equipment_and_services),
        health_care_providers_and_services("HEALTH CARE PROVIDERS & SERVICES", GicsIndustryGroupEnum.health_care_equipment_and_services),
        health_care_technology("HEALTH CARE TECHNOLOGY", GicsIndustryGroupEnum.health_care_equipment_and_services),
        biotechnology("BIOTECHNOLOGY", GicsIndustryGroupEnum.pharmaceuticals_biotechnology_and_life_sciences),
        pharmaceuticals("PHARMACEUTICALS", GicsIndustryGroupEnum.pharmaceuticals_biotechnology_and_life_sciences),
        life_sciences_tools_and_services("LIFE SCIENCES TOOLS & SERVICES", GicsIndustryGroupEnum.pharmaceuticals_biotechnology_and_life_sciences),
        banks("BANKS", GicsIndustryGroupEnum.banks),
        thrifts_and_mortgage_finance("THRIFTS & MORTGAGE FINANCE", GicsIndustryGroupEnum.banks),
        diversified_financial_services("DIVERSIFIED FINANCIAL SERVICES", GicsIndustryGroupEnum.diversified_financials),
        consumer_finance("CONSUMER FINANCE", GicsIndustryGroupEnum.diversified_financials),
        capital_markets("CAPITAL MARKETS", GicsIndustryGroupEnum.diversified_financials),
        mortgage_real_estate_investment_trusts("MORTGAGE REAL ESTATE INVESTMENT TRUSTS (REITS)", GicsIndustryGroupEnum.diversified_financials),
        insurance("INSURANCE", GicsIndustryGroupEnum.insurance),
        it_services("IT SERVICES", GicsIndustryGroupEnum.software_and_services),
        software("SOFTWARE", GicsIndustryGroupEnum.software_and_services),
        communications_equipment("COMMUNICATIONS EQUIPMENT", GicsIndustryGroupEnum.technology_hardware_and_equipment),
        technology_hardware_storage_and_peripherals("TECHNOLOGY HARDWARE, STORAGE & PERIPHERALS", GicsIndustryGroupEnum.technology_hardware_and_equipment),
        electronic_equipment_instruments_and_components("ELECTRONIC EQUIPMENT, INSTRUMENTS & COMPONENTS", GicsIndustryGroupEnum.technology_hardware_and_equipment),
        semiconductors_and_semiconductor_equipment("SEMICONDUCTORS & SEMICONDUCTOR EQUIPMENT", GicsIndustryGroupEnum.semiconductors_and_semiconductor_equipment),
        diversified_telecommunication_services("DIVERSIFIED TELECOMMUNICATION SERVICES", GicsIndustryGroupEnum.telecommunication_services),
        wireless_telecommunication_services("WIRELESS TELECOMMUNICATION SERVICES", GicsIndustryGroupEnum.telecommunication_services),
        media("MEDIA", GicsIndustryGroupEnum.media_and_entertainment),
        entertainment("ENTERTAINMENT", GicsIndustryGroupEnum.media_and_entertainment),
        interactive_media_and_services("INTERACTIVE MEDIA & SERVICES", GicsIndustryGroupEnum.media_and_entertainment),
        electric_utilities("ELECTRIC UTILITIES", GicsIndustryGroupEnum.utilities),
        gas_utilities("GAS UTILITIES", GicsIndustryGroupEnum.utilities),
        multi_utilities("MULTI-UTILITIES", GicsIndustryGroupEnum.utilities),
        water_utilities("WATER UTILITIES", GicsIndustryGroupEnum.utilities),
        independent_power_and_renewable_electricity_producers("INDEPENDENT POWER AND RENEWABLE ELECTRICITY PRODUCERS", GicsIndustryGroupEnum.utilities),
        equity_real_estate_investment_trusts("EQUITY REAL ESTATE INVESTMENT TRUSTS (REITS)", GicsIndustryGroupEnum.real_estate),
        real_estate_management_and_development("REAL ESTATE MANAGEMENT & DEVELOPMENT", GicsIndustryGroupEnum.real_estate),
        ;
        private static final Map<String, GicsIndustryEnum> enumByName = new HashMap<>(values().length, 1);
        static {
            for (GicsIndustryEnum c : values()) enumByName.put(c.name, c);
        }
        public static GicsIndustryEnum getByName(String name) throws IllegalArgumentException {
            GicsIndustryEnum result = enumByName.get(name);
            if (result == null) {
                throw new IllegalArgumentException("Invalid name: " + name);
            }
            return result;
        }

        public final String name;
        public final GicsIndustryGroupEnum gicsIndustryGroup;
        public String getGicsIndustryGroup() {
            return gicsIndustryGroup.name();
        }
        public String getCode(){
            return this.name();
        }


        GicsIndustryEnum(String name, GicsIndustryGroupEnum gicsIndustryGroup){
            this.name = name;
            this.gicsIndustryGroup = gicsIndustryGroup;
        }
    }

}
