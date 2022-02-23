package com.lib.database.entity.fund;

import com.lib.database.entity.fund.model.FundBasicView;
import com.lib.database.entity.fund.model.FundDetailView;
import com.lib.database.entity.fund.model.FundFinanceView;
import com.lib.database.entity.region.RegionHelper;
import com.lib.database.entity.sector.SectorHelper;
import com.lib.database.wrapper.AbstractDAO;
import com.lib.database.wrapper.InvestmentDatabaseWrapper;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Get fund only data
 */
public class FundDAO extends AbstractDAO {

    private final static Logger logger = LogManager.getLogger(FundDAO.class);

    public final static List<String> TICKER_FUND_TYPES = Arrays.asList("Fund", "ETF", "Mutual Fund");
    public final static List<String> ESG_LEVELS = Arrays.asList("High", "Medium", "Low"); // i would say esg-score of 50% is high, given its hard to find 80% score-rate. average would be 15% - 49%. Low = below 15%

    /**
     * Get list of funds (data: basic, finance data & exposure data) - filter by search params
     * - Also returns 'total number of un-paginated results'. Only search functions will provide this
     *
     * @return Pair < totalNumberOfRowsPossible, PaginatedResults >
     */
    public static Pair<Integer,List<FundDetailView>> searchFund(InvestmentDatabaseWrapper dbWrapper, String baseCurrencyCode,
                                                                int paginationOffset, int paginationLimit,
                                                                List<String> types,
                                                                List<RegionHelper.RegionEnum> regions,
                                                                List<SectorHelper.SectorEnum> sectors,
                                                                String searchText
    )throws SQLException {
        String prefixLog = "searchFund - ";

        Connection conn = dbWrapper.getConnection();
        List<FundDetailView> result;
        int totalNumResults = 0;

        // If 'overall_count' becomes slow, try 'James Moberg' way (but take out CTE) on https://stackoverflow.com/questions/12352471/getting-total-row-count-from-offset-fetch-next
        try(conn){
            StringBuilder queryBuilder = new StringBuilder("SELECT *, overall_count = COUNT(*) OVER() FROM " + FundDetailView.schemaName + "." + FundDetailView.tableName);
            List<Object> queryParams = new ArrayList<>();

            // filter specific base-currency (those without any price, will return 1 record with base_currency_code=null)
            queryBuilder.append(" WHERE (base_currency_code IS NULL OR base_currency_code = ?)");
            queryParams.add(baseCurrencyCode);

            // apply search filters
            if(types != null && types.size() > 0){
                queryBuilder.append(" AND ticker_type_name IN(");
                queryBuilder.append(getQuestionMarkParameters(types.size()));
                queryBuilder.append(")");

                queryParams.addAll(types);
            }

            if(regions != null && regions.size() > 0){

                for(RegionHelper.RegionEnum r: regions){
                    String postfixColumnName = r.toString();
                    queryBuilder.append(" AND (");

                    // filter is "rank 1" or "rank 2/3 with at least 10% in stake"
                    queryBuilder
                            .append(" regional_rank_").append(postfixColumnName).append(" = 1")
                            .append(" OR (regional_rank_").append(postfixColumnName).append(" IN (2,3) and regional_perc_").append(postfixColumnName).append(" >= 10)")
                            .append(")");
                }
            }

            if(sectors != null && sectors.size() > 0){

                for(SectorHelper.SectorEnum s: sectors){
                    String postfixColumnName = s.toString();
                    queryBuilder.append(" AND (");

                    // filter is "rank 1" or "rank 2/3 with at least 10% in stake"
                    queryBuilder
                            .append(" sector_rank_").append(postfixColumnName).append(" = 1")
                            .append(" OR (sector_rank_").append(postfixColumnName).append(" IN (2,3) and sector_perc_").append(postfixColumnName).append(" >= 10)")
                            .append(")");
                }
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

                        result.add(FundDetailView.createInstanceFromTableRow(rs, baseCurrencyCode));
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " row(s)");
                }
            }
        }
        return new Pair(totalNumResults, result);
    }

    /**
     * Get list of funds (data: basic, finance data & exposure data) - filter by id
     *
     * @param dbWrapper investment database wrapper
     * @param tickerIds List of ticker Ids
     */
    public static Map<Long, FundDetailView> getFundDetailViewsByIds(InvestmentDatabaseWrapper dbWrapper, String baseCurrencyCode, Collection<Long> tickerIds) throws SQLException {
        String prefixLog = "getFundDetailViewsByIds - ";

        Connection conn = dbWrapper.getConnection();
        Map<Long, FundDetailView> result = null;
        try(conn){

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + FundDetailView.schemaName + "." + FundDetailView.tableName);
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
                        FundDetailView t = FundDetailView.createInstanceFromTableRow(rs, baseCurrencyCode);
                        result.put(t.getTickerId(), t);
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " tickerView(s)");
                }
            }
        }
        return result;
    }


    /**
     * Get list of funds (data: basic & finance data) - filter by id
     *
     * @param dbWrapper investment database wrapper
     * @param tickerIds List of ticker Ids
     */
    public static Map<Long, FundFinanceView> getFundFinanceViewsByIds(InvestmentDatabaseWrapper dbWrapper, String baseCurrencyCode, Collection<Long> tickerIds) throws SQLException {
        String prefixLog = "getFundFinanceViewsByIds - ";

        Connection conn = dbWrapper.getConnection();
        Map<Long, FundFinanceView> result = null;
        try(conn){

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + FundFinanceView.schemaName + "." + FundFinanceView.tableName);
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
                        FundFinanceView t = FundFinanceView.createInstanceFromTableRow(rs, baseCurrencyCode);
                        result.put(t.getTickerId(), t);
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " tickerView(s)");
                }
            }
        }
        return result;
    }

    /**
     * Get list of funds (data: basic data only) - filter by id
     *
     * @param dbWrapper investment database wrapper
     * @param tickerIds List of ticker Ids
     */
    public static Map<Long, FundBasicView> getFundBasicViewsByIds(InvestmentDatabaseWrapper dbWrapper, Collection<Long> tickerIds) throws SQLException {
        String prefixLog = "getFundBasicViewsByIds - ";

        Connection conn = dbWrapper.getConnection();
        Map<Long, FundBasicView> result = null;
        try(conn){

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + FundBasicView.schemaName + "." + FundBasicView.tableName);
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
                        FundBasicView t = FundBasicView.createInstanceFromTableRow(rs);
                        result.put(t.getTickerId(), t);
                    }
                    logger.debug(prefixLog + "Returned " + result.size() + " tickerView(s)");
                }
            }
        }
        return result;
    }
}
