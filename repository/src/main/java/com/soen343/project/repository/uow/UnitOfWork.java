package com.soen343.project.repository.uow;

import com.soen343.project.database.connection.DatabaseConnector;
import com.soen343.project.database.connection.operation.DatabaseBatchOperation;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Kevin Tan 2018-10-18
 */

@SuppressWarnings("unchecked")
public class UnitOfWork{

    private Queue<DatabaseBatchOperation> commandBatchQueue;

    public UnitOfWork() {
        commandBatchQueue = new LinkedList<>();
    }

    public void commit() {
        DatabaseConnector.executeBatchUpdate((statement -> {
            while (!commandBatchQueue.isEmpty()) {
                commandBatchQueue.remove().execute(statement);
            }
        }));
    }

    public void registerOperation(DatabaseBatchOperation databaseBatchOperation) {
        this.commandBatchQueue.add(databaseBatchOperation);
    }

}
