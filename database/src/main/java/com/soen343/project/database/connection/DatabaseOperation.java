package com.soen343.project.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseOperation {
    void execute(Connection connection) throws SQLException;
}