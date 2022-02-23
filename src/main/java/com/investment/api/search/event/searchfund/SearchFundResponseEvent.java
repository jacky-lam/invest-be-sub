package com.investment.api.search.event.searchfund;

import com.lib.api.event.AbstractResponseEvent;
import com.lib.database.entity.fund.model.FundDetailView;

import java.util.List;

public class SearchFundResponseEvent extends AbstractResponseEvent {

    private final int totalUnpaginatedResults;
    private final Integer paginationOffset;
    private final Integer paginationLimit;
    private final List<FundDetailView> results;

    public SearchFundResponseEvent(int totalUnpaginatedResults, Integer paginationOffset, Integer paginationLimit,
                                   List<FundDetailView> results) {
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

    public List<FundDetailView> getResults() {
        return results;
    }


    @Override
    public String toString() {
        return "SearchFundResponseEvent{" +
                "totalUnpaginatedResults=" + totalUnpaginatedResults +
                ", paginationOffset=" + paginationOffset +
                ", paginationLimit=" + paginationLimit +
                ", results=" + results +
                '}';
    }
}