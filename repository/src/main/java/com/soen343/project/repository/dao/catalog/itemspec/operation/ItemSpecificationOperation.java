package com.soen343.project.repository.dao.catalog.itemspec.operation;

import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.repository.entity.catalog.Book;
import com.soen343.project.repository.entity.catalog.Magazine;
import com.soen343.project.repository.entity.catalog.Movie;
import com.soen343.project.repository.entity.catalog.Music;

import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.database.query.QueryBuilder.createDeleteQuery;
import static com.soen343.project.repository.entity.EntityConstants.*;

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
            statement.executeUpdate(createDeleteQuery(ITEM_TABLE, ITEMSPECID, movie.getId().toString(), TYPE, movie.getClass().getSimpleName() ));
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

    public static DatabaseBatchOperation magazineDeleteOperation(Magazine magazine) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(magazine.getTable(), magazine.getId()));
            statement.executeUpdate(createDeleteQuery(ITEM_TABLE, ITEMSPECID, magazine.getId().toString(), TYPE, magazine.getClass().getSimpleName() ));
        };
    }

    public static DatabaseBatchOperation musicDeleteOperation(Music music) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(music.getTable(), music.getId()));
            statement.executeUpdate(createDeleteQuery(ITEM_TABLE, ITEMSPECID, music.getId().toString(), TYPE, music.getClass().getSimpleName() ));
        };
    }

    public static DatabaseBatchOperation bookDeleteOperation(Book book) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(book.getTable(), book.getId()));
            statement.executeUpdate(createDeleteQuery(ITEM_TABLE, ITEMSPECID, book.getId().toString(), TYPE, book.getClass().getSimpleName() ));
        };
    }



}
