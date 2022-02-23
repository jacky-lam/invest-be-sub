package com.lib.api.database.model;

/**
 * Certain places might need
 */
public enum TickerDataLevelEnum {
    ID,
    BASIC, // data from general ticker table (name, symbol, region, currency)
    FINANCE, // basic common financial numbers (i.e. latest ticker price, returns, yield). enough for main portfolio page
    DETAIL, // Search page level (get timeseries data. i.e. region/sector breakdown)
    // COMPLETE // Ticker page level
}
