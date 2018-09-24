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

    public static void executeQuery(String query) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        //try-with-resource
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_PATH)) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Time-out if database does not execute any queries in 30 seconds
            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
