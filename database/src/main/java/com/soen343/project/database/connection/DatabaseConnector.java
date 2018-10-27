package com.soen343.project.database.connection;

import com.soen343.project.database.DatabaseConstants;
import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.database.connection.operation.DatabaseScript;

import java.sql.*;
import java.util.ArrayList;
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

    public static void executeBatchOperation(DatabaseBatchOperation databaseBatchOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            databaseBatchOperation.execute(statement); // Execute batch statements
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String update) {
        executeDatabaseScript(connection -> {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            statement.executeUpdate(update);
        });
    }

    public static List<Long> executeUpdateAndReturnIDs(String update) {
        List<Long> list = new ArrayList<>();
        executeDatabaseScript(connection -> {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            statement.executeUpdate(update);
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()){
                Long id = rs.getLong(1);
                if (id != 0){
                    list.add(id);
                }
            }
        });
        return list;
    }

    public static DatabaseEntity executeQuery(String query, DatabaseQueryOperation databaseQueryOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            return (DatabaseEntity) databaseQueryOperation.execute(statement.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<? extends DatabaseEntity> executeQueryExpectMultiple(String query, DatabaseQueryOperation databaseQueryOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            return (List<? extends DatabaseEntity>) databaseQueryOperation.execute(statement.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
