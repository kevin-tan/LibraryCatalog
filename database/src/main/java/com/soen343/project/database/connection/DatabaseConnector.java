package com.soen343.project.database.connection;

import com.soen343.project.database.DatabaseConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sagar Patel 2018-09-23
 */

public class DatabaseConnector {

    public interface DatabaseOperation {
        void execute(Connection connection) throws SQLException;
    }

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

    public static void executeQuery(String query) {
        executeDatabaseOperation(connection -> {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            statement.executeUpdate(query);
        });
    }
}
