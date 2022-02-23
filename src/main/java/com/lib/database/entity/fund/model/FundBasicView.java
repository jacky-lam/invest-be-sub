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
public class FundBasicView extends AbstractFundView {

    public static final String schemaName = "dbo";
    public static final String tableName = "vw_fund_basic";

    private final String isinCode;
    private final long tickerId;
    private final String tickerSymbol;
    private final String tickerName;
    private final Timestamp tickerStartDate;
    private final String tickerTypeName;
    private final String tickerCountry;
    private final String tickerCurrencyCode;

    //Read
    public FundBasicView(String isinCode, long tickerId, String tickerSymbol, String tickerName, String tickerTypeName,
                         Timestamp tickerStartDate, String tickerCountry, String tickerCurrencyCode) {
        this.isinCode = isinCode;
        this.tickerId = tickerId;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.tickerTypeName = tickerTypeName;
        this.tickerStartDate = tickerStartDate;
        this.tickerCountry = tickerCountry;
        this.tickerCurrencyCode = tickerCurrencyCode;
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
    public Date getTickerStartDate(){
        return tickerStartDate;
    }

    @JsonProperty("tickerStartDate")
    public String getTickerStartDateString() {
        return tickerStartDate != null ? df.format(tickerStartDate) : null;
    }

    public String getTickerCountry() {
        return tickerCountry;
    }

    public String getTickerCurrencyCode() {
        return tickerCurrencyCode;
    }

    // TODO: override `equals` & `hashCode`

    @Override
    public String toString() {
        return "FundBasicView{" +
                "isinCode='" + isinCode + '\'' +
                ", tickerId=" + tickerId +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", tickerTypeName='" + tickerTypeName + '\'' +
                ", tickerStartDate='" + tickerStartDate + '\'' +
                ", tickerCountry='" + tickerCountry + '\'' +
                ", tickerCurrencyCode='" + tickerCurrencyCode + '\'' +
                '}';
    }

    // Create from view row
    public static FundBasicView createInstanceFromTableRow(ResultSet rs) throws SQLException {

        String isinCode = rs.getString("isin_code");
        long tickerId = rs.getLong("ticker_id");
        String tickerSymbol = rs.getString("ticker_symbol");
        String tickerName = rs.getString("ticker_name");
        String tickerTypeName = rs.getString("ticker_type_name");
        Timestamp tickerStartDate = rs.getTimestamp("ticker_start_date");
        String tickerCountryIso = rs.getString("ticker_country_iso");
        String tickerCurrencyCode = rs.getString("ticker_currency_code");

        return new FundBasicView(
                isinCode, tickerId, tickerSymbol, tickerName, tickerTypeName, tickerStartDate,
                tickerCountryIso, tickerCurrencyCode
        );
    }
}