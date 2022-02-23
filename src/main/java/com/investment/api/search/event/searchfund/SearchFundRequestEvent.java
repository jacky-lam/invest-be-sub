package com.investment.api.search.event.searchfund;

import com.investment.api.search.SearchEventHandler;
import com.lib.api.event.AbstractResponseEvent;
import com.lib.api.event.ISelfDispatchRequestEvent;
import com.lib.database.entity.fund.FundDAO;
import com.lib.database.entity.fund.model.FundDetailView;
import com.lib.database.entity.region.RegionHelper;
import com.lib.database.entity.sector.SectorHelper;
import com.lib.database.entity.userprofile.model.UserProfile;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Search
 */
public class SearchFundRequestEvent implements ISelfDispatchRequestEvent<SearchEventHandler> {

    private final static Logger logger = LogManager.getLogger(SearchFundRequestEvent.class);

    private UserProfile user;
    private Integer paginationOffset;
    private Integer paginationLimit;
    private final List<String> tickerTypeNames;
    private final List<String> regionNames;
    private final List<String> sectorNames;
    private final String searchText;

    public SearchFundRequestEvent(UserProfile user, Integer paginationOffset, Integer paginationLimit, List<String> tickerTypeNames,
                                  List<String> regionNames, List<String> sectorNames, String searchText){
        this.user = user;
        this.paginationOffset = paginationOffset;
        this.paginationLimit = paginationLimit;
        this.tickerTypeNames = tickerTypeNames;
        this.regionNames = regionNames;
        this.sectorNames = sectorNames;
        this.searchText = searchText;
    }

    public ResponseEntity<AbstractResponseEvent> processEvent(SearchEventHandler eventHandler) throws ResponseStatusException {

        // get user's base currency code setting
        String userBaseCurrencyCode = user.getBaseCurrencyCode();

        // search text
        String searchingText = searchText != null && searchText.length() > 0 ? searchText : null;

        // validate params
        if(tickerTypeNames != null) {
            for (String t : tickerTypeNames) {
                if (!FundDAO.TICKER_FUND_TYPES.contains(t)) {
                    logger.error("Invalid tickerTypeNames filter: " + t + ".\n" + this.toString());
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid tickerTypeNames filter");
                }
            }
        }

        List<RegionHelper.RegionEnum> regionalEnums = null;
        if(regionNames != null){
            regionalEnums = new ArrayList<>();
            for(String r : regionNames) {
                try {
                    regionalEnums.add(RegionHelper.RegionEnum.valueOf(r));
                } catch (Exception e) {
                    logger.error("Invalid regionNames filter: " + r + ".\n" +this.toString(), e);
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid regionNames filter");
                }
            }
        }

        List<SectorHelper.SectorEnum> sectorEnums = null;
        if(sectorNames != null){
            sectorEnums = new ArrayList<>();
            for(String s : sectorNames) {
                try {
                    sectorEnums.add(SectorHelper.SectorEnum.valueOf(s));
                } catch (Exception e) {
                    logger.error("Invalid sectorNames filter: " + s + ".\n" +this.toString(), e);
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid sectorNames filter");
                }
            }
        }

        // setup pagination max/min values
        if(paginationOffset == null)
            paginationOffset = 0;
        else if(paginationOffset < 0)
            paginationOffset = 0;

        if(paginationLimit == null)
            paginationLimit = 50;
        else if(paginationLimit < 0)
            paginationLimit = 0;
        else if(paginationLimit > 1000) // if request is from an internal service: we should remove limit
            paginationLimit = 1000;

        try {
            Pair<Integer,List<FundDetailView>> result = FundDAO.searchFund(eventHandler.getDbWrapper(), userBaseCurrencyCode,
                    paginationOffset, paginationLimit, tickerTypeNames, regionalEnums, sectorEnums, searchingText
            );
            SearchFundResponseEvent responseEvent = new SearchFundResponseEvent(
                    result.getKey(),
                    paginationOffset,
                    paginationLimit,
                    result.getValue()
            );
            return new ResponseEntity<>(responseEvent, HttpStatus.OK);
        }
        catch(Exception e){
            logger.error("Failed querying search fund.\n" + this.toString(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem occurred searching fund");
        }
    }

    @Override
    public String toString() {
        return "SearchFundRequestEvent{" +
                "user=" + user +
                ", paginationOffset=" + paginationOffset +
                ", paginationLimit=" + paginationLimit +
                ", searchText=\"" + searchText + "\"" +
                ", tickerTypeNames=" + tickerTypeNames +
                ", regionNames=" + regionNames +
                ", sectorNames=" + sectorNames +
                '}';
    }
}
