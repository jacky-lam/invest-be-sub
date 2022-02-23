package com.investment.api.search;

import com.lib.database.wrapper.InvestmentDatabaseWrapper;
import com.lib.api.eventhandler.AbstractEventHandler;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

@Service("SearchEventHandler")
public class SearchEventHandler extends AbstractEventHandler<InvestmentDatabaseWrapper> {

    public SearchEventHandler(){
        super(LogManager.getLogger(SearchEventHandler.class), new InvestmentDatabaseWrapper());
    }
}
