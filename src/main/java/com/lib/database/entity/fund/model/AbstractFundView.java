package com.lib.database.entity.fund.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class AbstractFundView {

    protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    protected final static String[] trailingReturnsColumns = {
            "latest_trailing_returns_m1", "latest_trailing_returns_m3", "latest_trailing_returns_m6",
            "latest_trailing_returns_y1", "latest_trailing_returns_y3", "latest_trailing_returns_y5"
    };

}
