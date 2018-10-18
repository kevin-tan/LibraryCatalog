package com.soen343.project.database.connection.operation;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseScript {
    void execute(Connection connection) throws SQLException;
}