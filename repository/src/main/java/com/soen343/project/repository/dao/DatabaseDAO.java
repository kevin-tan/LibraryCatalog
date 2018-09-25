package com.soen343.project.repository.dao;

import com.soen343.project.repository.entity.DatabaseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
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

    }

    public void delete(DatabaseEntity entity) {

    }

    public void deleteAll() {

    }

    public void update(DatabaseEntity entity) {

    }

    public DatabaseEntity findById(Long id) {
        return null;
    }

    public List<DatabaseEntity> findAll() {
        return null;
    }

}
