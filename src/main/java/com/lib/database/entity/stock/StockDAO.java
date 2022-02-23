package com.lib.database.entity.stock;

import com.lib.database.entity.sector.SectorHelper;
import com.lib.database.entity.stock.model.StockBasicView;
import com.lib.database.entity.stock.model.StockFinanceView;
import com.lib.database.wrapper.AbstractDAO;
import com.lib.database.wrapper.InvestmentDatabaseWrapper;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Get Stock only data
 */
public class StockDAO extends AbstractDAO {

    private final static Logger logger = LogManager.getLogger(StockDAO.class);

    /**
     * Get list of stocks (data: basic, finance data & exposure data) - filter by search params
     * - Also returns 'total number of un-paginated results'. Only search functions will provide this
     *
     * @return Pair < totalNumberOfRowsPossible, PaginatedResults >
     */
    public static Pair<Integer,List<StockFinanceView>> searchStock(InvestmentDatabaseWrapper dbWrapper, String baseCurrencyCode,
                                                                    int paginationOffset, int paginationLimit,
                                                                    Collection<String> countryIsos,
                                                                    Collection<SectorHelper.GicsSectorEnum> gicsSectors,
                                                                    Collection<SectorHelper.GicsIndustryGroupEnum> gicsIndustryGroups,
                                                                    Collection<SectorHelper.GicsIndustryEnum> gicsIndustries,
                                                                    String searchText
    )throws SQLException {
        String prefixLog = "searchStock - ";

        Connection conn = dbWrapper.getConnection();
        List<StockFinanceView> result;
        int totalNumResults = 0;

        // If 'overall_count' becomes slow, try 'James Moberg' way (but take out CTE) on https://stackoverflow.com/questions/12352471/getting-total-row-count-from-offset-fetch-next
        try(conn){
            StringBuilder queryBuilder = new StringBuilder("SELECT *, overall_count = COUNT(*) OVER() FROM " + StockFinanceView.schemaName + "." + StockFinanceView.tableName);
            List<Object> queryParams = new ArrayList<>();

            // filter specific base-currency (those without any price, will return 1 record with base_currency_code=null)
            queryBuilder.append(" WHERE (base_currency_code IS NULL OR base_currency_code = ?)");
            queryParams.add(baseCurrencyCode);

            // apply search filters
            if(countryIsos != null && countryIsos.size() > 0){
                queryBuilder.append(" AND ticker_country_iso IN (" + getQuestionMarkParameters(countryIsos.size())+")");
                queryParams.addAll(countryIsos);
            }

            // apply GIC filters
            boolean hasGicSectors = gicsSectors != null && gicsSectors.size() > 0;
            boolean hasGicIndustryGroups = gicsIndustryGroups != null && gicsIndustryGroups.size() > 0;
            boolean hasGicIndustries = gicsIndustries != null && gicsIndustries.size() > 0;
            if(hasGicSectors || hasGicIndustryGroups || hasGicIndustries){

                queryBuilder.append(" AND (");
                boolean isFirst = true;

                if(hasGicSectors) {
                    queryBuilder.append(" ticker_gic_sector IN (" + getQuestionMarkParameters(gicsSectors.size()) +")");
                    for (SectorHelper.GicsSectorEnum s : gicsSectors) {
                        queryParams.add(s.getCode());
                    }
                    isFirst = false;
                }
                if(hasGicIndustryGroups) {
                    queryBuilder.append(isFirst ? "" : " OR" );
                    queryBuilder.append(" ticker_gic_group IN (" + getQuestionMarkParameters(gicsIndustryGroups.size()) +")");
                    for (SectorHelper.GicsIndustryGroupEnum s : gicsIndustryGroups) {
                        queryParams.add(s.getCode());
                    }
                    isFirst = false;
                }
                if(hasGicIndustries) {
                    queryBuilder.append(isFirst ? "" : " OR" );
                    queryBuilder.append(" ticker_gic_industry IN (" + getQuestionMarkParameters(gicsIndustries.size()) +")");
                    for (SectorHelper.GicsIndustryEnum s : gicsIndustries) {
                        queryParams.add(s.getCode());
                    }
                    isFirst = false;
                }

                queryBuilder.append(")");
            }

            if(searchText != null && searchText.length() > 0){
                queryBuilder.append(" AND (ticker_name LIKE ? OR ticker_symbol LIKE ? OR isin_code LIKE ?)");
                queryParams.add("%"+searchText+"%");
                queryParams.add("%"+searchText+"%");
                queryParams.add("%"+searchText+"%");
            }

            // order
            // - TODO: in future, we shall order by popularity (e.g. combination: volume traded, asset size, ..., ticker_name)
            queryBuilder.append(" ORDER BY ticker_name");

            // limit
            queryBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            queryParams.add(paginationOffset);
            queryParams.add(paginationLimit);

            String query = queryBuilder.toString();
            logger.debug(prefixLog + "Executing query: " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            int paramIndex = 0;
            for(Object param: queryParams){
                pstmt.setObject(++paramIndex, param);
            }

            try(pstmt){
                pstmt.execute();
                ResultSet rs = pstmt.getResultSet();
                try(rs){
                    result = new ArrayList<>();
                    while(rs.next()){
                        totalNumResults = rs.getInt("overall_count");

                        result.add(StockFinanceView.createInstanceFromTableRow(rs, baseCurrencyCode));
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " row(s)");
                }
            }
        }
        return new Pair(totalNumResults, result);
    }

    /**
     * Get list of stocks (data: basic, finance data & exposure data) - filter by id
     *
     * @param dbWrapper investment database wrapper
     * @param tickerIds List of ticker Ids
     */
    public static Map<Long, StockFinanceView> getStockFinanceViewsByIds(InvestmentDatabaseWrapper dbWrapper, String baseCurrencyCode, Collection<Long> tickerIds) throws SQLException {
        String prefixLog = "getStockFinanceViewsByIds - ";

        Connection conn = dbWrapper.getConnection();
        Map<Long, StockFinanceView> result = null;
        try(conn){

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + StockFinanceView.schemaName + "." + StockFinanceView.tableName);
            List<Object> queryParams = new ArrayList<>();

            // filter specific base-currency (those without any price, will return 1 record with base_currency_code=null)
            queryBuilder.append(" WHERE (base_currency_code IS NULL OR base_currency_code = ?)");
            queryParams.add(baseCurrencyCode);

            // filter by tickerIds
            if(tickerIds != null && tickerIds.size() > 0){
                queryBuilder.append(" AND ticker_id IN(");
                queryBuilder.append(getQuestionMarkParameters(tickerIds.size()));
                queryBuilder.append(")");

                queryParams.addAll(tickerIds);
            }

            String query = queryBuilder.toString();
            logger.debug(prefixLog + "Executing query: "+query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            int paramIndex = 0;
            for(Object param: queryParams){
                pstmt.setObject(++paramIndex, param);
            }

            try(pstmt){
                pstmt.execute();
                ResultSet rs = pstmt.getResultSet();
                try(rs){
                    result = new HashMap<>();
                    while(rs.next()){
                        StockFinanceView t = StockFinanceView.createInstanceFromTableRow(rs, baseCurrencyCode);
                        result.put(t.getTickerId(), t);
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " row(s)");
                }
            }
        }
        return result;
    }

    /**
     * Get list of stocks (data: basic data only) - filter by id
     *
     * @param dbWrapper investment database wrapper
     * @param tickerIds List of ticker Ids
     */
    public static Map<Long, StockBasicView> getStockBasicViewsByIds(InvestmentDatabaseWrapper dbWrapper, Collection<Long> tickerIds) throws SQLException {
        String prefixLog = "getStockBasicViewsByIds - ";

        Connection conn = dbWrapper.getConnection();
        Map<Long, StockBasicView> result = null;
        try(conn){

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + StockBasicView.schemaName + "." + StockBasicView.tableName);
            List<Object> queryParams = new ArrayList<>();

            // filter by tickerIds
            if(tickerIds != null && tickerIds.size() > 0){
                queryBuilder.append(" WHERE ticker_id IN(");
                queryBuilder.append(getQuestionMarkParameters(tickerIds.size()));
                queryBuilder.append(")");

                queryParams.addAll(tickerIds);
            }

            String query = queryBuilder.toString();
            logger.debug(prefixLog + "Executing query: "+query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            int paramIndex = 0;
            for(Object param: queryParams){
                pstmt.setObject(++paramIndex, param);
            }
            try(pstmt){
                pstmt.execute();
                ResultSet rs = pstmt.getResultSet();
                try(rs){
                    result = new HashMap<>();
                    while(rs.next()){
                        StockBasicView t = StockBasicView.createInstanceFromTableRow(rs);
                        result.put(t.getTickerId(), t);
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " row(s)");
                }
            }
        }
        return result;
    }
}
