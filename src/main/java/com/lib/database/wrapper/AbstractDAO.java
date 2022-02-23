package com.lib.database.wrapper;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCSVFileRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import de.bytefish.jsqlserverbulkinsert.SqlServerBulkInsert;
import de.bytefish.jsqlserverbulkinsert.mapping.AbstractMapping;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public abstract class AbstractDAO {

    protected static long createTransaction(Logger logger, AbstractDatabaseWrapper dbWrapper, String transactionName, String transactionDescription) throws Exception {
        if(transactionName == null)
            throw new IllegalArgumentException("Cannot create transaction with null transactionName");

        logger.debug("createTransaction - executing: [audit].[sp_create_transaction]...");
        long transactionId = -1;
        try(Connection conn = dbWrapper.getConnection()) {
            try (CallableStatement cstmt = conn.prepareCall("{call [audit].[sp_create_transaction](?, ?, ?)}")){
                cstmt.setString("name", transactionName);
                cstmt.setString("description", transactionDescription);
                cstmt.registerOutParameter("transaction_id", Types.BIGINT);
                cstmt.execute();
                transactionId = cstmt.getLong("transaction_id");
            }
        }
        logger.debug("createTransaction - successfully created: transactionId("+transactionId+")");
        return transactionId;
    }

    // Generate the ? for "IN(...)"
    protected static String getQuestionMarkParameters(int size){
        if(size <= 0)
            return "";
        if(size == 1)
            return "?";

        String s = "?,".repeat(size);
        return s.substring(0, s.length()-1);
    }

    protected static <T> void bulkInsertObject(Logger logger, AbstractDatabaseWrapper dbWrapper, AbstractMapping<T> mapping, Collection<T> objects) throws Exception {
        Connection conn = dbWrapper.getSingletonRawConnection();

        SqlServerBulkInsert<T> bulkInsert = new SqlServerBulkInsert<>(mapping);
        logger.debug("bulkInsertObject - Writing "+objects.size()+" item(s)...");
        SQLServerBulkCopyOptions options = new SQLServerBulkCopyOptions();
        options.setBulkCopyTimeout((30*60)); //30 minutes (if exceed 30 minutes, we should think about upgrading cpu)
        options.setBatchSize(1000); //prevent heap load
        bulkInsert.saveAll(conn, options, objects.stream());
        logger.debug("bulkInsertObject - Finished "+objects.size()+" item(s)!");
    }

    protected void bulkInsertCsvFile(Logger logger, AbstractDatabaseWrapper dbWrapper, String databaseTableName, SQLServerBulkCSVFileRecord fileRecord, SQLServerBulkCopyOptions copyOptions) throws Exception{
        // example: https://stackoverflow.com/questions/41144967/how-to-use-the-sql-server-jdbc-bulk-copy-api

        logger.debug("Begin bulkInsertCsvFile...");
        Connection conn = dbWrapper.getConnection();
        try(conn) {
            logger.debug("bulkInsertCsvFile - Preparing file input...");
            try (SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(conn)) {
                if(copyOptions != null) bulkCopy.setBulkCopyOptions(copyOptions);
                bulkCopy.setDestinationTableName(databaseTableName);
                logger.debug("bulkInsertCsvFile - Writing...");
                bulkCopy.writeToServer(fileRecord);
            }
            logger.debug("bulkInsertCsvFile - Finished!");
        }
    }

    protected void bulkInsertCsvString(Logger logger, AbstractDatabaseWrapper dbWrapper, String databaseTableName, String csvString, String csvDelimiter, boolean firstLineIsColumnNames, Collection<ColumnMetadata> csvColumnMetadata, SQLServerBulkCopyOptions copyOptions) throws Exception {

        logger.debug("Begin bulkInsertCsvString...");

        // example : https://myadventuresincoding.wordpress.com/2018/04/01/java-using-sqlserverbulkcopy-in-java-with-an-inputstream/
        Connection conn = dbWrapper.getConnection();
        try(conn) {

            logger.debug("bulkInsertCsvString - Preparing input...");
            byte[] bytes = csvString.getBytes(StandardCharsets.UTF_8);
            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {

                // Pass in input stream and set column information
                SQLServerBulkCSVFileRecord fileRecord = new SQLServerBulkCSVFileRecord(
                        inputStream, StandardCharsets.UTF_8.name(), csvDelimiter, firstLineIsColumnNames);
                for (ColumnMetadata cm : csvColumnMetadata){
                    fileRecord.addColumnMetadata(cm.getPositionInSource(), cm.getName(), cm.getJdbcType(), cm.getPrecision(), cm.getScale(), cm.getDateTimeFormatter());
                }

                logger.debug("bulkInsertCsvString - Writing...");
                // Write the CSV document to the database table
                try (SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(conn)) {
                    if(copyOptions != null) bulkCopy.setBulkCopyOptions(copyOptions);
                    bulkCopy.setDestinationTableName(databaseTableName);
                    bulkCopy.writeToServer(fileRecord);
                }
                logger.debug("bulkInsertCsvString - Finished!");
            }
        } catch (Exception e) {
            logger.error("Failed to bulkInsertCsvString", e);
            throw e;
        }
    }
    public class ColumnMetadata{
        private final int positionInSource;
        private final String name;
        private final int jdbcType;
        private final int precision;
        private final int scale;
        private final DateTimeFormatter dateTimeFormatter;

        public ColumnMetadata(int positionInSource, String name, int jdbcType, int precision, int scale) {
            this(positionInSource, name, jdbcType, precision, scale, null);
        }
        public ColumnMetadata(int positionInSource, String name, int jdbcType, int precision, int scale, DateTimeFormatter dateTimeFormatter) {
            this.positionInSource = positionInSource;
            this.name = name;
            this.jdbcType = jdbcType;
            this.precision = precision;
            this.scale = scale;
            this.dateTimeFormatter = dateTimeFormatter;
        }

        public int getPositionInSource() {
            return positionInSource;
        }

        public String getName() {
            return name;
        }

        public int getJdbcType() {
            return jdbcType;
        }

        public int getPrecision() {
            return precision;
        }

        public int getScale() {
            return scale;
        }

        public DateTimeFormatter getDateTimeFormatter() {
            return dateTimeFormatter;
        }
    }
}
