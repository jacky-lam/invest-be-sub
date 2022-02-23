package com.lib.database.wrapper;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections for one specific database
 *
 *  Lib:  https://github.com/brettwooldridge/HikariCP
 */
public class DatabaseConnectionWrapper {

    private final static Logger logger = LogManager.getLogger(DatabaseConnectionWrapper.class);

    private final HikariDataSource ds;
    private Connection singlePermantRawConn; //this is a raw connection, as ConnectionPools never return the actual JDBC Connection object you want

    private final String databaseName;
    private final String jdbcDriverClassname;
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DatabaseConnectionWrapper(String databaseName, String jdbcDriverClassname, String jdbcUrl, String username, String password){
        this.databaseName = databaseName;
        this.jdbcDriverClassname = jdbcDriverClassname;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;

        ds = new HikariDataSource();
        ds.setMaximumPoolSize(5);
        ds.setDataSourceClassName(jdbcDriverClassname);
        ds.addDataSourceProperty("url", jdbcUrl);
        ds.addDataSourceProperty("user", username);
        ds.addDataSourceProperty("password", password);
        ds.setInitializationFailFast(true);
        ds.setPoolName(databaseName+"ConnectionPool");
    }

    public Connection getConnection() throws SQLException {
        logger.debug("Getting connection: " + databaseName);
        return ds.getConnection();
    }

    /*
    * Get raw Connection object. IMPORTANT - Developer must never close this (as other threads maybe using it)
    *
    * */
    public Connection getSingletonRawConnection() throws ClassNotFoundException, SQLException {
        logger.debug("Getting raw connection: " + databaseName);

        if(singlePermantRawConn == null || singlePermantRawConn.isClosed()){
            Class.forName(this.jdbcDriverClassname);
            Properties properties = new Properties();
            properties.put("user", this.username);
            properties.put("password", this.password);

            singlePermantRawConn = DriverManager.getConnection(this.jdbcUrl, properties);
        }
        return singlePermantRawConn;
    }

    public void terminateAllConnections(){
        logger.debug("Terminating all " + this.databaseName + " database connections...");
        if(singlePermantRawConn != null){
            try {
                singlePermantRawConn.close();
            }
            catch(Exception err){
                logger.error("Problem closing " + this.databaseName + " singlePermantRawConn", err);
            }
        }
        ds.shutdown();
        logger.debug("Terminated all " + this.databaseName + " database connections!");
    }
}
