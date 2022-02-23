package com.lib.database.entity.userprofile;

import com.lib.database.entity.userprofile.model.UserProfile;
import com.lib.database.wrapper.AbstractDAO;
import com.lib.database.wrapper.InvestmentDatabaseWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/*
*
* Manages transaction related to user profile
*
* */
public class UserProfileDAO extends AbstractDAO {

    private final static Logger logger = LogManager.getLogger(UserProfileDAO.class);

    public static long login(InvestmentDatabaseWrapper dbWrapper, String loginName, String password) throws SQLException {

        long result = -1;

        try(Connection conn = dbWrapper.getConnection()) {

            String query = "call [dbo].[usp_login_user_profile](?, ?, ?, ?)";
            logger.debug("Executing login for \""+loginName+"\": " + query);
            try (CallableStatement cstmt = conn.prepareCall("{"+query+"}")){
                cstmt.setString("login_name", loginName);
                cstmt.setString("p_password", password);
                cstmt.registerOutParameter("user_id", Types.BIGINT);
                cstmt.registerOutParameter("response_message", Types.NVARCHAR);
                cstmt.execute();
                long userId = cstmt.getLong("user_id");
                String responseMessage = cstmt.getString("response_message");
                logger.debug("Login result for \""+loginName+"\": userId=" + userId +", responseMessage="+responseMessage);

                if(userId>0) {
                    result = userId;
                }
            }
        }

        return result;
    }

    public static UserProfile getUserProfileByLoginName(InvestmentDatabaseWrapper dbWrapper, String loginName) throws SQLException {

        UserProfile result = null;
        String prefixLog = "getUserProfileByLoginName - ";

        Connection conn = dbWrapper.getConnection();
        try (conn) {
            String query = "SELECT * FROM " + UserProfile.schemaName + "." + UserProfile.tableName + " WHERE login_name=?";
            logger.debug(prefixLog + "Executing query: " + query + ". Parameters:" + loginName);

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, loginName);
                pstmt.execute();
                ResultSet rs = pstmt.getResultSet();
                try (rs) {
                    if (rs.next()) {
                        result = UserProfile.createInstanceFromTableRow(rs);
                    }
                    logger.debug(prefixLog + "Retrieved user (userId:" + result.getId() + ")");
                }
            }
        }
        return result;
    }

}
