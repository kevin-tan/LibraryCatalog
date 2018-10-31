package com.soen343.project.repository.dao.catalog.itemspec.operation;

import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.repository.entity.catalog.Movie;

import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.database.query.QueryBuilder.createDeleteQuery;
import static com.soen343.project.repository.entity.EntityConstants.MOVIEID;

/**
 * Created by Kevin Tan 2018-10-30
 */
public final class ItemSpecificationOperation {

    private ItemSpecificationOperation() {}

    public static DatabaseBatchOperation movieSaveOperation(Movie movie) {
        return statement -> {
            statement.executeUpdate(createSaveQuery(movie.getTableWithColumns(), movie.toSQLValue()));
            movie.setId(statement.executeQuery(GET_ID_MOST_RECENT).getInt(MOST_RECENT_ID_COL));
            statement.executeUpdate(createSaveQuery(movie.getProducersTableWithColumns(), movie.getProducersSQLValues()));
            statement.executeUpdate(createSaveQuery(movie.getActorsTableWithColumns(), movie.getActorsSQLValues()));
            statement.executeUpdate(createSaveQuery(movie.getDubbedTableWithColumns(), movie.getDubbedSQLValues()));
        };
    }

    public static DatabaseBatchOperation movieDeleteOperation(Movie movie) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(movie.getTable(), movie.getId()));
            statement.executeUpdate(createDeleteQuery(movie.getProducersTable(), MOVIEID, movie.getId().toString()));
            statement.executeUpdate(createDeleteQuery(movie.getActorsTable(), MOVIEID, movie.getId().toString()));
            statement.executeUpdate(createDeleteQuery(movie.getDubbedTable(), MOVIEID, movie.getId().toString()));
        };
    }

    public static DatabaseBatchOperation movieUpdateOperation(Movie movie) {
        return statement -> {
            statement.executeUpdate(createUpdateQuery(movie.getTable(), movie.sqlUpdateValues(), movie.getId()));
            statement.executeUpdate(createDeleteQuery(movie.getProducersTable(), MOVIEID, movie.getId().toString()));
            statement.executeUpdate(createDeleteQuery(movie.getActorsTable(), MOVIEID, movie.getId().toString()));
            statement.executeUpdate(createDeleteQuery(movie.getDubbedTable(), MOVIEID, movie.getId().toString()));
            statement.executeUpdate(createSaveQuery(movie.getProducersTableWithColumns(), movie.getProducersSQLValues()));
            statement.executeUpdate(createSaveQuery(movie.getActorsTableWithColumns(), movie.getActorsSQLValues()));
            statement.executeUpdate(createSaveQuery(movie.getDubbedTableWithColumns(), movie.getDubbedSQLValues()));
        };
    }



}
