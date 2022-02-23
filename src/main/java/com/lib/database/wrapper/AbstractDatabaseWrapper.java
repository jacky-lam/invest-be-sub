package com.lib.database.wrapper;

import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * Single monitor of creating connection to specific database
 * Concrete class handles fetching the connection details
 */
public abstract class AbstractDatabaseWrapper {

    private final Logger logger;
    private final DatabaseConnectionWrapper connWrapper;

    public AbstractDatabaseWrapper(Logger logger, DatabaseConnectionWrapper connWrapper){
        this.logger = logger;
        this.connWrapper = connWrapper;
    }

    public Connection getConnection() throws SQLException {
        return connWrapper.getConnection();
    }

    public Connection getSingletonRawConnection() throws Exception {
        return connWrapper.getSingletonRawConnection();
    }

    public void terminateAllConnections(){
        connWrapper.terminateAllConnections();
    }
}
