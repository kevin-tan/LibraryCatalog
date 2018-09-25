package com.soen343.project.database.connection;


import com.soen343.project.database.base.DatabaseEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseQueryOperation {

    DatabaseEntity execute(ResultSet rs) throws SQLException;

}