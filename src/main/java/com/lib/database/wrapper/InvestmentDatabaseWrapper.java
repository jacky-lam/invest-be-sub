package com.lib.database.wrapper;


import org.apache.logging.log4j.LogManager;

public class InvestmentDatabaseWrapper extends AbstractDatabaseWrapper {


    public InvestmentDatabaseWrapper(){

        //databaseserver-investment.co4oringhhiw.ap-northeast-1.rds.amazonaws.com
        //admin
        //JackyKingsley123

        //localhost
        //sa
        //Password123!

        //todo: store connection details elsewhere
        super(LogManager.getLogger(InvestmentDatabaseWrapper.class),
                new DatabaseConnectionWrapper(
                        "investment",
                        "com.microsoft.sqlserver.jdbc.SQLServerDataSource",
                        "jdbc:sqlserver://localhost:1433;databaseName=investment;selectMethod=cursor",
                        "sa",
                        "Password123!"
                )
        );
    }
}
