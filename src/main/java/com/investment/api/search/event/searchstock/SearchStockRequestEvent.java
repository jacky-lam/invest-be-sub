package com.investment.api.search.event.searchstock;

import com.investment.api.search.SearchEventHandler;
import com.lib.api.event.AbstractResponseEvent;
import com.lib.api.event.ISelfDispatchRequestEvent;
import com.lib.database.entity.sector.SectorHelper;
import com.lib.database.entity.stock.StockDAO;
import com.lib.database.entity.stock.model.StockFinanceView;
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
public class SearchStockRequestEvent implements ISelfDispatchRequestEvent<SearchEventHandler> {

    private final static Logger logger = LogManager.getLogger(SearchStockRequestEvent.class);

    private UserProfile user;
    private Integer paginationOffset;
    private Integer paginationLimit;
    private final List<String> countryISOs;
    List<String> gicsSectors;
    List<String> gicsIndustryGroups;
    List<String> gicsIndustries;
    private final String searchText;

    public SearchStockRequestEvent(UserProfile user, Integer paginationOffset, Integer paginationLimit,
                                   List<String> countryISOs,
                                   List<String> gicsSectors, List<String> gicsIndustryGroups, List<String> gicsIndustries,
                                   String searchText){
        this.user = user;
        this.paginationOffset = paginationOffset;
        this.paginationLimit = paginationLimit;
        this.countryISOs = countryISOs;
        this.gicsSectors = gicsSectors;
        this.gicsIndustryGroups = gicsIndustryGroups;
        this.gicsIndustries = gicsIndustries;
        this.searchText = searchText;
    }

    public ResponseEntity<AbstractResponseEvent> processEvent(SearchEventHandler eventHandler) throws ResponseStatusException {

        // get user's base currency code setting
        String userBaseCurrencyCode = user.getBaseCurrencyCode();

        // search text
        String searchingText = searchText != null && searchText.length() > 0 ? searchText : null;

        // validate gic sector values
        List<SectorHelper.GicsSectorEnum> gicsSectorsEnum = null;
        if(gicsSectors != null){
            gicsSectorsEnum = new ArrayList<>();
            for(String s : gicsSectors) {
                try {
                    gicsSectorsEnum.add(SectorHelper.GicsSectorEnum.valueOf(s));
                } catch (Exception e) {
                    logger.error("Invalid gicsSectors filter: " + s + ".\n" +this.toString(), e);
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid gicsSectors filter");
                }
            }
        }

        // validate gic industry-group values
        List<SectorHelper.GicsIndustryGroupEnum> gicsIndustryGroupsEnum = null;
        if(gicsIndustryGroups != null){
            gicsIndustryGroupsEnum = new ArrayList<>();
            for(String s : gicsIndustryGroups) {
                try {
                    gicsIndustryGroupsEnum.add(SectorHelper.GicsIndustryGroupEnum.valueOf(s));
                } catch (Exception e) {
                    logger.error("Invalid gicsIndustryGroups filter: " + s + ".\n" +this.toString(), e);
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid gicsIndustryGroups filter");
                }
            }
        }

        // validate gic industry values
        List<SectorHelper.GicsIndustryEnum> gicsIndustriesEnum = null;
        if(gicsIndustries != null){
            gicsIndustriesEnum = new ArrayList<>();
            for(String s : gicsIndustries) {
                try {
                    gicsIndustriesEnum.add(SectorHelper.GicsIndustryEnum.valueOf(s));
                } catch (Exception e) {
                    logger.error("Invalid gicsIndustries filter: " + s + ".\n" +this.toString(), e);
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid gicsIndustries filter");
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
            Pair<Integer,List<StockFinanceView>> result = StockDAO.searchStock(
                    eventHandler.getDbWrapper(), userBaseCurrencyCode,
                    paginationOffset, paginationLimit,
                    countryISOs,
                    gicsSectorsEnum, gicsIndustryGroupsEnum, gicsIndustriesEnum,
                    searchingText
            );
            SearchStockResponseEvent responseEvent = new SearchStockResponseEvent(
                    result.getKey(),
                    paginationOffset,
                    paginationLimit,
                    result.getValue()
            );
            return new ResponseEntity<>(responseEvent, HttpStatus.OK);
        }
        catch(Exception e){
            logger.error("Failed querying search stock.\n" + this.toString(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem occurred searching stock");
        }
    }

    @Override
    public String toString() {
        return "SearchStockRequestEvent{" +
                "user=" + user +
                ", paginationOffset=" + paginationOffset +
                ", paginationLimit=" + paginationLimit +
                ", searchText=\"" + searchText + "\"" +
                ", countryISOs=" + countryISOs +
                ", gicsSectors=" + gicsSectors +
                ", gicsIndustryGroups=" + gicsIndustryGroups +
                ", gicsIndustries=" + gicsIndustries +
                '}';
    }
}
