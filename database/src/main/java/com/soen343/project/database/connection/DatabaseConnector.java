package com.soen343.project.database.connection;

import com.soen343.project.database.DatabaseConstants;
import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.database.connection.operation.DatabaseBatchQueryOperation;
import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.database.connection.operation.DatabaseScript;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Sagar Patel 2018-09-23
 */

public class DatabaseConnector {

    private DatabaseConnector() {}

    static {
        // Load JDBC driver when DatabaseConnector class is loaded
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void executeDatabaseScript(DatabaseScript databaseScript) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            databaseScript.execute(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeBatchUpdate(DatabaseBatchOperation databaseBatchOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            databaseBatchOperation.execute(createStatement(connection)); // Execute batch statements
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String update) {
        executeDatabaseScript(connection -> {
            createStatement(connection).executeUpdate(update);
        });
    }

    public static <T> T exectuteBatchQuery(DatabaseBatchQueryOperation<T> batchQueryOp) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            return batchQueryOp.execute(createStatement(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T executeForeignKeyTableQuery(String query, DatabaseQueryOperation<T> databaseQueryOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            Statement statement = createStatement(connection);
            return databaseQueryOperation.execute(statement.executeQuery(query), statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T executeQuery(String query, DatabaseQueryOperation<T> databaseQueryOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            Statement statement = createStatement(connection);
            return databaseQueryOperation.execute(statement.executeQuery(query), statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> executeQueryExpectMultiple(String query, DatabaseQueryOperation<List<T>> databaseQueryOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            Statement statement = createStatement(connection);
            return databaseQueryOperation.execute(statement.executeQuery(query), statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> executeQueryExpectMultiple(DatabaseBatchQueryOperation<List<T>> databaseQueryOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            return databaseQueryOperation.execute(createStatement(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Statement createStatement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
        return statement;
    }
}
