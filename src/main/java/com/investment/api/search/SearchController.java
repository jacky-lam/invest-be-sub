package com.investment.api.search;

import com.investment.api.search.event.searchfund.SearchFundRequestEvent;
import com.investment.api.search.event.searchstock.SearchStockRequestEvent;
import com.lib.api.event.AbstractResponseEvent;
import com.lib.database.entity.userprofile.model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Api related to tickers
 *
 */

// in prod, you must want to app-level control of the endpoint exposure
@CrossOrigin(origins = "*", allowedHeaders = "*") // enable all endpoints for this controller (can annotate on method-level for granular)
@RestController("SearchController")
@RequestMapping("/search")
public class SearchController {
    private final static Logger logger = LogManager.getLogger(SearchController.class);

    @Resource(name="SearchEventHandler")
    SearchEventHandler eventHandler;

    @GetMapping(value = {"/funds"}, produces = "application/json")
    public ResponseEntity<AbstractResponseEvent> funds(@RequestHeader(required = true, name="Authorization") String token,
                                                       @RequestParam(required = false) Integer paginationOffset,
                                                       @RequestParam(required = false) Integer paginationLimit,
                                                       @RequestParam(required = false) List<String> tickerTypeNames,
                                                       @RequestParam(required = false) List<String> regionNames,
                                                       @RequestParam(required = false) List<String> sectorNames,
                                                       @RequestParam(required = false) String searchText
    ) throws ResponseStatusException {
        UserProfile user = eventHandler.getAuthenticationHelper().getUserFromAuthorisationValueElseThrowResponse(eventHandler.getDbWrapper(), token);
        return eventHandler.processSelfDispatchEvent(
                new SearchFundRequestEvent(
                        user, paginationOffset, paginationLimit,
                        tickerTypeNames, regionNames, sectorNames, searchText
                )
        );
    }

    @GetMapping(value = {"/stocks"}, produces = "application/json")
    public ResponseEntity<AbstractResponseEvent> stocks(@RequestHeader(required = true, name="Authorization") String token,
                                                        @RequestParam(required = false) Integer paginationOffset,
                                                        @RequestParam(required = false) Integer paginationLimit,
                                                        @RequestParam(required = false) List<String> gicsSectors,
                                                        @RequestParam(required = false) List<String> gicsIndustryGroups,
                                                        @RequestParam(required = false) List<String> gicsIndustries,
                                                        @RequestParam(required = false) List<String> countryISOs,
                                                        @RequestParam(required = false) String searchText
    ) throws ResponseStatusException {
        UserProfile user = eventHandler.getAuthenticationHelper().getUserFromAuthorisationValueElseThrowResponse(eventHandler.getDbWrapper(), token);
        return eventHandler.processSelfDispatchEvent(
                new SearchStockRequestEvent(
                        user, paginationOffset, paginationLimit,
                        countryISOs,
                        gicsSectors, gicsIndustryGroups, gicsIndustries,
                        searchText
                )
        );
    }
}
