package com.soen343.project.database.connection;

import com.soen343.project.database.DatabaseConstants;
import com.soen343.project.database.base.DatabaseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Sagar Patel 2018-09-23
 */

public class DatabaseConnector {

    private DatabaseConnector(){}

    static {
        // Load JDBC driver when DatabaseConnector class is loaded
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void executeDatabaseOperation(DatabaseOperation databaseOperation) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
            databaseOperation.execute(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String update) {
        executeDatabaseOperation(connection -> {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            statement.executeUpdate(update);
        });
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
