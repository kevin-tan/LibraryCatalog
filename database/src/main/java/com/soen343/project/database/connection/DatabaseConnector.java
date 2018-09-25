package com.soen343.project.database.connection;

import com.soen343.project.database.DatabaseConstants;
import com.soen343.project.database.base.DatabaseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sagar Patel 2018-09-23
 */

public class DatabaseConnector {

    public static void executeDatabaseOperation(DatabaseOperation databaseOperation) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
                databaseOperation.execute(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
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
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL)) {
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds

                return databaseQueryOperation.execute(statement.executeQuery(query));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Should never be reached
        return null;
    }
}
