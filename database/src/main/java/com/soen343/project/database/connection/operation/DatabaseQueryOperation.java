package com.soen343.project.database.connection.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface DatabaseQueryOperation <T>{

    T execute(ResultSet rs, Statement statement) throws SQLException;

}