package com.lib.database.entity.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lib.database.entity.fund.model.AbstractFundView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/*
*
* Database table model for view
*   - ReadOnly object
*   - Client-friendly data
* */
public class StockFinanceView extends AbstractFundView {

    public static final String schemaName = "ts";
    public static final String tableName = "vw_stock_finance";

    private final String isinCode;
    private final long tickerId;
    private final String tickerSymbol;
    private final String tickerName;
    private final Timestamp tickerIpoDate;
    private final String tickerTypeName;
    private final String tickerCountry;
    private final String tickerCurrencyCode;
    private final String tickerGicSector;
    private final String tickerGicGroup;
    private final String tickerGicIndustry;
    private final String tickerGicSubIndustry;
    private final Double tickerLocalLatestDailyClosePrice;
    private final String baseCurrencyCode;
    private final Double localToBaseFxRate;
    private final Double tickerBaseLatestDailyClosePrice;

    //Read
    public StockFinanceView(String isinCode, long tickerId, String tickerSymbol, String tickerName,
                            Timestamp tickerIpoDate, String tickerTypeName, String tickerCountry,
                            String tickerCurrencyCode,
                            String tickerGicSector, String tickerGicGroup, String tickerGicIndustry,
                            String tickerGicSubIndustry, Double tickerLocalLatestDailyClosePrice,
                            String baseCurrencyCode, Double localToBaseFxRate, Double tickerBaseLatestDailyClosePrice) {
        this.isinCode = isinCode;
        this.tickerId = tickerId;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.tickerIpoDate = tickerIpoDate;
        this.tickerTypeName = tickerTypeName;
        this.tickerCountry = tickerCountry;
        this.tickerCurrencyCode = tickerCurrencyCode;
        this.tickerGicSector = tickerGicSector;
        this.tickerGicGroup = tickerGicGroup;
        this.tickerGicIndustry = tickerGicIndustry;
        this.tickerGicSubIndustry = tickerGicSubIndustry;
        this.tickerLocalLatestDailyClosePrice = tickerLocalLatestDailyClosePrice;
        this.baseCurrencyCode = baseCurrencyCode;
        this.localToBaseFxRate = localToBaseFxRate;
        this.tickerBaseLatestDailyClosePrice = tickerBaseLatestDailyClosePrice;
    }

    // Create from view row
    public static StockFinanceView createInstanceFromTableRow(ResultSet rs, String expectedBaseCurrencyCode) throws SQLException {

        String isinCode = rs.getString("isin_code");
        long tickerId = rs.getLong("ticker_id");
        String tickerSymbol = rs.getString("ticker_symbol");
        String tickerName = rs.getString("ticker_name");
        String tickerTypeName = rs.getString("ticker_type_name");
        Timestamp tickerIpoDate = rs.getTimestamp("ticker_ipo_date");
        String tickerCountryIso = rs.getString("ticker_country_iso");
        String tickerCurrencyCode = rs.getString("ticker_currency_code");
        String tickerGicSector = rs.getString("ticker_gic_sector");
        String tickerGicGroup = rs.getString("ticker_gic_group");
        String tickerGicIndustry = rs.getString("ticker_gic_industry");
        String tickerGicSubIndustry = rs.getString("ticker_gic_sub_industry");
        Double tickerLocalLatestDailyClosePrice = rs.getDouble("ticker_local_latest_daily_close_price"); if(rs.wasNull()) tickerLocalLatestDailyClosePrice = null;
        String baseCurrencyCode = rs.getString("base_currency_code"); if(baseCurrencyCode == null) baseCurrencyCode = expectedBaseCurrencyCode;
        Double localToBaseFxRate = rs.getDouble("local_to_base_fx_rate"); if(rs.wasNull()) localToBaseFxRate = null;
        Double tickerBaseLatestDailyClosePrice = rs.getDouble("base_latest_daily_close_price"); if(rs.wasNull()) tickerBaseLatestDailyClosePrice = null;

        return new StockFinanceView(
                isinCode, tickerId, tickerSymbol, tickerName, tickerIpoDate, tickerTypeName,
                tickerCountryIso, tickerCurrencyCode,
                tickerGicSector, tickerGicGroup, tickerGicIndustry, tickerGicSubIndustry,
                tickerLocalLatestDailyClosePrice, baseCurrencyCode, localToBaseFxRate, tickerBaseLatestDailyClosePrice
        );
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

    public String getTickerTypeName() {
        return tickerTypeName;
    }

    @JsonIgnore
    public Date getTickerIpoDate(){
        return tickerIpoDate;
    }

    @JsonProperty("tickerIpoDate")
    public String getTickerIpoDateString() {
        return tickerIpoDate != null ? df.format(tickerIpoDate) : null;
    }

    public String getTickerCountry() {
        return tickerCountry;
    }

    public String getTickerCurrencyCode() {
        return tickerCurrencyCode;
    }

    public String getTickerGicSector() {
        return tickerGicSector;
    }

    public String getTickerGicGroup() {
        return tickerGicGroup;
    }

    public String getTickerGicIndustry() {
        return tickerGicIndustry;
    }

    public String getTickerGicSubIndustry() {
        return tickerGicSubIndustry;
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

    // TODO: override `equals` & `hashCode`


    @Override
    public String toString() {
        return "StockFinanceView{" +
                "isinCode='" + isinCode + '\'' +
                ", tickerId=" + tickerId +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", tickerIpoDate=" + tickerIpoDate +
                ", tickerTypeName='" + tickerTypeName + '\'' +
                ", tickerCountry='" + tickerCountry + '\'' +
                ", tickerCurrencyCode='" + tickerCurrencyCode + '\'' +
                ", tickerGicSector='" + tickerGicSector + '\'' +
                ", tickerGicGroup='" + tickerGicGroup + '\'' +
                ", tickerGicIndustry='" + tickerGicIndustry + '\'' +
                ", tickerGicSubIndustry='" + tickerGicSubIndustry + '\'' +
                ", tickerLocalLatestDailyClosePrice=" + tickerLocalLatestDailyClosePrice +
                ", baseCurrencyCode='" + baseCurrencyCode + '\'' +
                ", localToBaseFxRate=" + localToBaseFxRate +
                ", tickerBaseLatestDailyClosePrice=" + tickerBaseLatestDailyClosePrice +
                '}';
    }
}