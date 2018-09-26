package com.soen343.project.database.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseQueryOperation <T>{

    T execute(ResultSet rs) throws SQLException;

}