package com.lib.database.entity.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lib.database.entity.region.RegionHelper;
import com.lib.database.entity.sector.SectorHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
*
* Database table model for view
*   - ReadOnly object
*   - Client-friendly data
* */
public class FundDetailView extends AbstractFundView {

    public static final String schemaName = "ts";
    public static final String tableName = "vw_fund_detail";

    private final String isinCode;
    private final long tickerId;
    private final String tickerSymbol;
    private final String tickerName;
    private final Timestamp tickerStartDate;
    private final String tickerTypeName;
    private final String tickerCountry;
    private final String tickerCurrencyCode;
    private final Double tickerLocalLatestDailyClosePrice;
    private final String baseCurrencyCode;
    private final Double tickerBaseLatestDailyClosePrice;
    private final Double localToBaseFxRate;
    private final Double tickerLatestOngoingCharge;
    private final Double tickerLatestDailyDividendYield;
    private final Double tickerLatestTotalNetAsset;

    private final Map<String, Double> latestTrailingReturns;
    private final Map<String, Double> regionalPercentage;
    private final Map<String, Double> sectorPercentage;
    private final Map<String, Double> productAllocationPercentage;

    //Read
    public FundDetailView(String isinCode, long tickerId, String tickerSymbol, String tickerName,
                          Timestamp tickerStartDate, String tickerTypeName, String tickerCountry,
                          String tickerCurrencyCode, Double tickerLocalLatestDailyClosePrice,
                          String baseCurrencyCode, Double localToBaseFxRate, Double tickerBaseLatestDailyClosePrice,
                          Double tickerLatestOngoingCharge, Double tickerLatestDailyDividendYield,
                          Double tickerLatestTotalNetAsset, Map<String, Double> latestTrailingReturns,
                          Map<String, Double> regionalPercentage, Map<String, Double> sectorPercentage,
                          Map<String, Double> productAllocationPercentage) {
        this.isinCode = isinCode;
        this.tickerId = tickerId;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.tickerStartDate = tickerStartDate;
        this.tickerTypeName = tickerTypeName;
        this.tickerCountry = tickerCountry;
        this.tickerCurrencyCode = tickerCurrencyCode;
        this.tickerLocalLatestDailyClosePrice = tickerLocalLatestDailyClosePrice;
        this.baseCurrencyCode = baseCurrencyCode;
        this.localToBaseFxRate = localToBaseFxRate;
        this.tickerBaseLatestDailyClosePrice = tickerBaseLatestDailyClosePrice;
        this.tickerLatestOngoingCharge = tickerLatestOngoingCharge;
        this.tickerLatestDailyDividendYield = tickerLatestDailyDividendYield;
        this.tickerLatestTotalNetAsset = tickerLatestTotalNetAsset;
        this.latestTrailingReturns = latestTrailingReturns;
        this.regionalPercentage = regionalPercentage;
        this.sectorPercentage = sectorPercentage;
        this.productAllocationPercentage = productAllocationPercentage;
    }

    public String getIsinCode() {
        return isinCode;
    }

    public long getTickerId() {
        return tickerId;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    @JsonIgnore
    public Date getTickerStartDate(){
        return tickerStartDate;
    }

    @JsonProperty("tickerStartDate")
    public String getTickerStartDateString() {
        return tickerStartDate != null ? df.format(tickerStartDate) : null;
    }

    public String getTickerTypeName() {
        return tickerTypeName;
    }

    public String getTickerCountry() {
        return tickerCountry;
    }

    public String getTickerCurrencyCode() {
        return tickerCurrencyCode;
    }

    public Double getTickerLocalLatestDailyClosePrice() {
        return tickerLocalLatestDailyClosePrice;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public Double getLocalToBaseFxRate() {
        return localToBaseFxRate;
    }

    public Double getTickerBaseLatestDailyClosePrice() {
        return tickerBaseLatestDailyClosePrice;
    }

    public Double getTickerLatestOngoingCharge() {
        return tickerLatestOngoingCharge;
    }

    public Double getTickerLatestDailyDividendYield() {
        return tickerLatestDailyDividendYield;
    }

    public Double getTickerLatestTotalNetAsset() {
        return tickerLatestTotalNetAsset;
    }

    public Map<String, Double> getLatestTrailingReturns() {
        return latestTrailingReturns;
    }

    public Map<String, Double> getRegionalPercentage() {
        return regionalPercentage;
    }

    public Map<String, Double> getSectorPercentage() {
        return sectorPercentage;
    }

    public Map<String, Double> getProductAllocationPercentage() {
        return productAllocationPercentage;
    }

    // TODO: override `equals` & `hashCode`


    @Override
    public String toString() {
        return "FundDetailView{" +
                "isinCode='" + isinCode + '\'' +
                ", tickerId=" + tickerId +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", tickerStartDate=" + tickerStartDate +
                ", tickerTypeName='" + tickerTypeName + '\'' +
                ", tickerCountry='" + tickerCountry + '\'' +
                ", tickerCurrencyCode='" + tickerCurrencyCode + '\'' +
                ", tickerLocalLatestDailyClosePrice=" + tickerLocalLatestDailyClosePrice +
                ", baseCurrencyCode='" + baseCurrencyCode + '\'' +
                ", localToBaseFxRate=" + localToBaseFxRate +
                ", tickerBaseLatestDailyClosePrice=" + tickerBaseLatestDailyClosePrice +
                ", tickerLatestOngoingCharge=" + tickerLatestOngoingCharge +
                ", tickerLatestDailyDividendYield=" + tickerLatestDailyDividendYield +
                ", tickerLatestTotalNetAsset=" + tickerLatestTotalNetAsset +
                ", latestTrailingReturns=" + latestTrailingReturns +
                ", regionalPercentage=" + regionalPercentage +
                ", sectorPercentage=" + sectorPercentage +
                ", productAllocationPercentage=" + productAllocationPercentage +
                '}';
    }

    // Create from view row
    public static FundDetailView createInstanceFromTableRow(ResultSet rs, String expectedBaseCurrencyCode) throws SQLException {

        String isinCode = rs.getString("isin_code");
        long tickerId = rs.getLong("ticker_id");
        String tickerSymbol = rs.getString("ticker_symbol");
        String tickerName = rs.getString("ticker_name");
        String tickerTypeName = rs.getString("ticker_type_name");
        Timestamp tickerStartDate = rs.getTimestamp("ticker_start_date");
        String tickerCountryIso = rs.getString("ticker_country_iso");
        String tickerCurrencyCode = rs.getString("ticker_currency_code");
        Double tickerLocalLatestDailyClosePrice = rs.getDouble("ticker_local_latest_daily_close_price"); if(rs.wasNull()) tickerLocalLatestDailyClosePrice = null;
        String baseCurrencyCode = rs.getString("base_currency_code"); if(baseCurrencyCode == null) baseCurrencyCode = expectedBaseCurrencyCode;
        Double localToBaseFxRate = rs.getDouble("local_to_base_fx_rate"); if(rs.wasNull()) localToBaseFxRate = null;
        Double tickerBaseLatestDailyClosePrice = rs.getDouble("base_latest_daily_close_price"); if(rs.wasNull()) tickerBaseLatestDailyClosePrice = null;
        Double tickerLatestOngoingCharge = rs.getDouble("latest_ongoing_charge"); if(rs.wasNull()) tickerLatestOngoingCharge = null;
        Double tickerLatestTotalNetAsset = rs.getDouble("latest_total_net_asset");  if(rs.wasNull()) tickerLatestTotalNetAsset = null;
        Double tickerLatestDailyDividendYield = rs.getDouble("latest_daily_dividend_yield"); if(rs.wasNull()) tickerLatestDailyDividendYield = null;

        Map<String, Double> latestTrailingReturns = new HashMap<>();
        for(String s : trailingReturnsColumns){
            Double value = rs.getDouble(s);
            if(rs.wasNull())
                value = null;
            latestTrailingReturns.put(s, value);
        }

        Map<String, Double> regionalPercentage = new HashMap<>();
        regionalPercentage.put(RegionHelper.RegionEnum.north_america.toString(), rs.getDouble("regional_perc_north_america"));
        regionalPercentage.put(RegionHelper.RegionEnum.united_kingdom.toString(), rs.getDouble("regional_perc_united_kingdom"));
        regionalPercentage.put(RegionHelper.RegionEnum.europe_developed.toString(), rs.getDouble("regional_perc_europe_developed"));
        regionalPercentage.put(RegionHelper.RegionEnum.europe_emerging.toString(), rs.getDouble("regional_perc_europe_emerging"));
        regionalPercentage.put(RegionHelper.RegionEnum.africa_middle_east.toString(), rs.getDouble("regional_perc_africa_middle_east"));
        regionalPercentage.put(RegionHelper.RegionEnum.japan.toString(), rs.getDouble("regional_perc_japan"));
        regionalPercentage.put(RegionHelper.RegionEnum.australasia.toString(), rs.getDouble("regional_perc_australasia"));
        regionalPercentage.put(RegionHelper.RegionEnum.asia_developed.toString(), rs.getDouble("regional_perc_asia_developed"));
        regionalPercentage.put(RegionHelper.RegionEnum.asia_emerging.toString(), rs.getDouble("regional_perc_asia_emerging"));
        regionalPercentage.put(RegionHelper.RegionEnum.latin_america.toString(), rs.getDouble("regional_perc_latin_america"));

        Map<String, Double> sectorPercentage = new HashMap<>();
        sectorPercentage.put(SectorHelper.SectorEnum.basic_materials.toString(), rs.getDouble("sector_perc_basic_materials"));
        sectorPercentage.put(SectorHelper.SectorEnum.consumer_cyclicals.toString(), rs.getDouble("sector_perc_consumer_cyclicals"));
        sectorPercentage.put(SectorHelper.SectorEnum.financial_services.toString(), rs.getDouble("sector_perc_financial_services"));
        sectorPercentage.put(SectorHelper.SectorEnum.real_estate.toString(), rs.getDouble("sector_perc_real_estate"));
        sectorPercentage.put(SectorHelper.SectorEnum.communication_services.toString(), rs.getDouble("sector_perc_communication_services"));
        sectorPercentage.put(SectorHelper.SectorEnum.energy.toString(), rs.getDouble("sector_perc_energy"));
        sectorPercentage.put(SectorHelper.SectorEnum.industrials.toString(), rs.getDouble("sector_perc_industrials"));
        sectorPercentage.put(SectorHelper.SectorEnum.technology.toString(), rs.getDouble("sector_perc_technology"));
        sectorPercentage.put(SectorHelper.SectorEnum.consumer_defensive.toString(), rs.getDouble("sector_perc_consumer_defensive"));
        sectorPercentage.put(SectorHelper.SectorEnum.healthcare.toString(), rs.getDouble("sector_perc_healthcare"));
        sectorPercentage.put(SectorHelper.SectorEnum.utilities.toString(), rs.getDouble("sector_perc_utilities"));

        Map<String, Double> productAllocationPercentage = new HashMap<>();
        productAllocationPercentage.put("stock", rs.getDouble("prod_perc_stock"));
        productAllocationPercentage.put("bond", rs.getDouble("prod_perc_bond"));
        productAllocationPercentage.put("cash", rs.getDouble("prod_perc_cash"));
        productAllocationPercentage.put("other", rs.getDouble("prod_perc_other"));
        productAllocationPercentage.put("unknown", rs.getDouble("prod_perc_unknown"));

        return new FundDetailView(
                isinCode, tickerId, tickerSymbol, tickerName, tickerStartDate, tickerTypeName,
                tickerCountryIso, tickerCurrencyCode,
                tickerLocalLatestDailyClosePrice,
                baseCurrencyCode, localToBaseFxRate, tickerBaseLatestDailyClosePrice,
                tickerLatestOngoingCharge, tickerLatestDailyDividendYield,
                tickerLatestTotalNetAsset, latestTrailingReturns,
                regionalPercentage, sectorPercentage, productAllocationPercentage
        );
    }
}