package com.soen343.project.repository.uow;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.connection.DatabaseConnector;
import com.soen343.project.database.query.QueryBuilder;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Kevin Tan 2018-10-18
 */

@SuppressWarnings("unchecked")
public class UnitOfWork<E extends DatabaseEntity> {

    private Queue<String> commandQueue;

    public UnitOfWork() {
        commandQueue = new LinkedList<>();
    }

    public void commit() {
        DatabaseConnector.executeBatchOperation((statement -> {
            while(!commandQueue.isEmpty()){
                statement.executeUpdate(commandQueue.remove());
            }
        }));
    }

    public void registerCreate(E entity) {
        this.commandQueue.add(QueryBuilder.createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    public void registerDelete(E entity) {
        this.commandQueue.add(QueryBuilder.createDeleteQuery(entity.getTable(), entity.getId()));
    }

    public void registerUpdate(E entity) {
        this.commandQueue.add(QueryBuilder.createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }

}
