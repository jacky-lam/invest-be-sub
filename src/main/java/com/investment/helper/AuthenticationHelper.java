package com.investment.helper;

import com.investment.security.JwtTokenUtil;
import com.investment.security.JwtUser;
import com.lib.database.entity.userprofile.UserProfileDAO;
import com.lib.database.entity.userprofile.model.UserProfile;
import com.lib.database.wrapper.InvestmentDatabaseWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Single place that manages all the JWT logic for our APIs
 * - Don't want the APIs to be importing JWT libraries (isolate the dependencies)
 * - The APIs shouldn't care what authentication tool is put in place
 */
@Component
public class AuthenticationHelper {

    private final static Logger logger = LogManager.getLogger(AuthenticationHelper.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Get user-id from authentication value (e.g. "Bearer xxx")
     *
     * @return Returns user object, otherwise throw http error
     */
    public UserProfile getUserFromAuthorisationValueElseThrowResponse(InvestmentDatabaseWrapper databaseWrapper, String authenticationValue) throws ResponseStatusException {
        String prefixLog = "getUserFromTokenElseThrowResponse - ";
        logger.debug(prefixLog + "Loading user details from token...");

        String token = authenticationValue.replace("Bearer ", "");
        String userName = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        if(userName != null){
            try{
                UserProfile user = UserProfileDAO.getUserProfileByLoginName(databaseWrapper, userName);
                if(user != null){
                    return user;
                }
                else{
                    logger.error(prefixLog + "Unrecognised username that bypassed the JWT Authorisation - " + userName);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
                }
            }catch (Exception e){
                logger.error(prefixLog + "Failed to load userId from database for the token's username - " + userName, e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was a problem validating your session. Please try again later");
            }
        }
        logger.error(prefixLog + "No username was found in token that bypassed the JWT Authorisation");
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
    }

    /**
     * Authenticate the user by validating its login-name and password
     *
     * @param databaseWrapper database to user-profile table
     * @param loginName
     * @param password
     * @return user id
     * @throws SQLException - error in running database execution scripts
     */
    public long authenticateUser(InvestmentDatabaseWrapper databaseWrapper, String loginName, String password) throws SQLException {
        return UserProfileDAO.login(databaseWrapper, loginName, password);
    }

    /**
     * Create header value
     */
    public String generateToken(String loginName){
        JwtUser userDetails = new JwtUser(loginName, new ArrayList<>());
        return "Bearer " + jwtTokenUtil.generateToken(userDetails);
    }
}
