package com.soen343.project.database.connection.operation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Kevin Tan 2018-10-18
 */
@FunctionalInterface
public interface DatabaseBatchOperation {

    void execute(Statement statement) throws SQLException;

}
