package com.investment.api.search.event.searchstock;

import com.lib.api.event.AbstractResponseEvent;
import com.lib.database.entity.fund.model.FundDetailView;
import com.lib.database.entity.stock.model.StockFinanceView;

import java.util.List;

public class SearchStockResponseEvent extends AbstractResponseEvent {

    private final int totalUnpaginatedResults;
    private final Integer paginationOffset;
    private final Integer paginationLimit;
    private final List<StockFinanceView> results;

    public SearchStockResponseEvent(int totalUnpaginatedResults, Integer paginationOffset, Integer paginationLimit,
                                    List<StockFinanceView> results) {
        this.totalUnpaginatedResults = totalUnpaginatedResults;
        this.paginationOffset = paginationOffset;
        this.paginationLimit = paginationLimit;
        this.results = results;
    }

    public int getTotalUnpaginatedResults() {
        return totalUnpaginatedResults;
    }

    public Integer getPaginationOffset() {
        return paginationOffset;
    }

    public Integer getPaginationLimit() {
        return paginationLimit;
    }

    public List<StockFinanceView> getResults() {
        return results;
    }


    @Override
    public String toString() {
        return "SearchStockResponseEvent{" +
                "totalUnpaginatedResults=" + totalUnpaginatedResults +
                ", paginationOffset=" + paginationOffset +
                ", paginationLimit=" + paginationLimit +
                ", results=" + results +
                '}';
    }
}