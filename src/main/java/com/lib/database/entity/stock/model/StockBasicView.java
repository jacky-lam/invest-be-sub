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
public class StockBasicView extends AbstractFundView {

    public static final String schemaName = "dbo";
    public static final String tableName = "vw_stock_basic";

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

    //Read
    public StockBasicView(String isinCode, long tickerId, String tickerSymbol, String tickerName, String tickerTypeName,
                          Timestamp tickerIpoDate, String tickerCountry, String tickerCurrencyCode,
                          String tickerGicSector, String tickerGicGroup, String tickerGicIndustry, String tickerGicSubIndustry) {
        this.isinCode = isinCode;
        this.tickerId = tickerId;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.tickerTypeName = tickerTypeName;
        this.tickerIpoDate = tickerIpoDate;
        this.tickerCountry = tickerCountry;
        this.tickerCurrencyCode = tickerCurrencyCode;
        this.tickerGicSector = tickerGicSector;
        this.tickerGicGroup = tickerGicGroup;
        this.tickerGicIndustry = tickerGicIndustry;
        this.tickerGicSubIndustry = tickerGicSubIndustry;
    }

    // Create from view row
    public static StockBasicView createInstanceFromTableRow(ResultSet rs) throws SQLException {

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

        return new StockBasicView(
                isinCode, tickerId, tickerSymbol, tickerName, tickerTypeName, tickerIpoDate,
                tickerCountryIso, tickerCurrencyCode,
                tickerGicSector, tickerGicGroup, tickerGicIndustry, tickerGicSubIndustry
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

    // TODO: override `equals` & `hashCode`


    @Override
    public String toString() {
        return "StockBasicView{" +
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
                '}';
    }
}