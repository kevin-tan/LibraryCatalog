package com.soen343.project.repository.dao;

import com.soen343.project.repository.entity.DatabaseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.executeQuery;
import static com.soen343.project.database.query.QueryBuilder.*;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Component
public class DatabaseDAO {

    public DatabaseDAO() { }

    public void save(DatabaseEntity entity) {
        executeQuery(createSaveQuery(entity.getTable(), entity.toSQLValue()));
    }

    public void saveAll(DatabaseEntity... entities) {
        //TODO Cleanup to execute single query instead of multiple
        for (DatabaseEntity entity : entities) {
            save(entity);
        }
    }

    public void delete(DatabaseEntity entity) {
        executeQuery(createDeleteQuery(entity.getTable(), entity.getId()));
    }

    public void update(DatabaseEntity entity) {
        executeQuery(createUpdateQuery(entity.getTable(), entity.sqlColumnValues(), entity.getId()));
    }

    public DatabaseEntity findById(Long id) {
        return null;
    }

    public List<DatabaseEntity> findAll() {
        return null;
    }

}
