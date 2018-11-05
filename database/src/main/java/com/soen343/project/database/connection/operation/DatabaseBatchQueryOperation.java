package com.soen343.project.database.connection.operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Kevin Tan 2018-11-02
 */

@FunctionalInterface
public interface DatabaseBatchQueryOperation<T> {

    T execute(Statement statement) throws SQLException;

}
