package com.lib.database.entity.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class FundFinanceView extends AbstractFundView {

    public static final String schemaName = "ts";
    public static final String tableName = "vw_fund_finance";

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
    private final Double localToBaseFxRate;
    private final Double tickerBaseLatestDailyClosePrice;
    private final Double tickerLatestOngoingCharge;
    private final Double tickerLatestDailyDividendYield;
    private final Double tickerLatestTotalNetAsset;

    private final Map<String, Double> latestTrailingReturns;

    //Read


    public FundFinanceView(String isinCode, long tickerId, String tickerSymbol, String tickerName,
                           Timestamp tickerStartDate, String tickerTypeName, String tickerCountry,
                           String tickerCurrencyCode, Double tickerLocalLatestDailyClosePrice,
                           String baseCurrencyCode, Double localToBaseFxRate, Double tickerBaseLatestDailyClosePrice,
                           Double tickerLatestOngoingCharge, Double tickerLatestDailyDividendYield,
                           Double tickerLatestTotalNetAsset, Map<String, Double> latestTrailingReturns) {
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

    // TODO: override `equals` & `hashCode`


    @Override
    public String toString() {
        return "FundFinanceView{" +
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
                '}';
    }

    // Create from view row
    public static FundFinanceView createInstanceFromTableRow(ResultSet rs, String expectedBaseCurrencyCode) throws SQLException {

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
        Double tickerLatestTotalNetAsset = rs.getDouble("latest_total_net_asset");  if(rs.wasNull()) tickerLatestTotalNetAsset = null;
        Double tickerLatestOngoingCharge = rs.getDouble("latest_ongoing_charge"); if(rs.wasNull()) tickerLatestOngoingCharge = null;
        Double tickerLatestDailyDividendYield = rs.getDouble("latest_daily_dividend_yield"); if(rs.wasNull()) tickerLatestDailyDividendYield = null;

        Map<String, Double> latestTrailingReturns = new HashMap<>();
        for(String s : trailingReturnsColumns){
            Double value = rs.getDouble(s);
            if(rs.wasNull())
                value = null;
            latestTrailingReturns.put(s, value);
        }

        return new FundFinanceView(
                isinCode, tickerId, tickerSymbol, tickerName,
                tickerStartDate, tickerTypeName, tickerCountryIso,
                tickerCurrencyCode, tickerLocalLatestDailyClosePrice,
                baseCurrencyCode, localToBaseFxRate, tickerBaseLatestDailyClosePrice,
                tickerLatestOngoingCharge, tickerLatestDailyDividendYield,
                tickerLatestTotalNetAsset, latestTrailingReturns
        );
    }
}