package com.soen343.project.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Kevin Tan 2018-09-23
 */
public class DatabaseConnector {

    public static void executeQuery(String query) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        //try-with-resource
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:LibraryCatalog.db")) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
