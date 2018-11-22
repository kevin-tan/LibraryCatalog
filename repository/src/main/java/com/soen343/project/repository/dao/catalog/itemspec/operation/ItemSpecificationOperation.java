package com.soen343.project.repository.dao.catalog.itemspec.operation;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.connection.operation.DatabaseBatchOperation;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

/**
 * Created by Kevin Tan 2018-10-30
 */
public final class ItemSpecificationOperation {

    private final static int COLUMNINDEX = 3;

    private ItemSpecificationOperation() {}

    public static DatabaseBatchOperation movieSaveOperation(Movie movie) {
        return statement -> {
            ResultSet rs = statement.executeQuery(createFindByAttributeQuery(movie.getTable(), TITLE, movie.getTitle()));
            if (rs.next()) {
                movie.setId(rs.getLong(ID));
                movie.setProducers(findAllFromForeignKey(statement, movie.getProducersTable(), movie.getId()));
                movie.setActors(findAllFromForeignKey(statement, movie.getActorsTable(), movie.getId()));
                movie.setDubbed(findAllFromForeignKey(statement, movie.getDubbedTable(), movie.getId()));
            }
            else {
                statement.executeUpdate(createSaveQuery(movie.getTableWithColumns(), movie.toSQLValue()));
                movie.setId(statement.executeQuery(GET_ID_MOST_RECENT)
                        .getInt(MOST_RECENT_ID_COL));
                statement.executeUpdate(createSaveQuery(movie.getProducersTableWithColumns(), movie.getProducersSQLValues()));
                statement.executeUpdate(createSaveQuery(movie.getActorsTableWithColumns(), movie.getActorsSQLValues()));
                statement.executeUpdate(createSaveQuery(movie.getDubbedTableWithColumns(), movie.getDubbedSQLValues()));
            }
        };
    }

    public static DatabaseBatchOperation movieDeleteOperation(Movie movie) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(movie.getTable(), movie.getId()));
            statement.executeUpdate(createDeleteQuery(movie.getProducersTable(), MOVIEID, movie.getId()
                    .toString()));
            statement.executeUpdate(createDeleteQuery(movie.getActorsTable(), MOVIEID, movie.getId()
                    .toString()));
            statement.executeUpdate(createDeleteQuery(movie.getDubbedTable(), MOVIEID, movie.getId()
                    .toString()));
            executeDeleteLoanable(statement, movie);
        };
    }

    public static DatabaseBatchOperation movieUpdateOperation(Movie movie) {
        return statement -> {
            statement.executeUpdate(createUpdateQuery(movie.getTable(), movie.sqlUpdateValues(), movie.getId()));
            statement.executeUpdate(createDeleteQuery(movie.getProducersTable(), MOVIEID, movie.getId()
                    .toString()));
            statement.executeUpdate(createDeleteQuery(movie.getActorsTable(), MOVIEID, movie.getId()
                    .toString()));
            statement.executeUpdate(createDeleteQuery(movie.getDubbedTable(), MOVIEID, movie.getId()
                    .toString()));
            statement.executeUpdate(createSaveQuery(movie.getProducersTableWithColumns(), movie.getProducersSQLValues()));
            statement.executeUpdate(createSaveQuery(movie.getActorsTableWithColumns(), movie.getActorsSQLValues()));
            statement.executeUpdate(createSaveQuery(movie.getDubbedTableWithColumns(), movie.getDubbedSQLValues()));
        };
    }

    private static void executeDeleteLoanable(Statement statement, DatabaseEntity databaseEntity) throws SQLException {
        statement.execute("DELETE FROM LoanableItem WHERE id = (SELECT id FROM Item WHERE itemSpecId = " + databaseEntity.getId() +
                          " and Item.type = '" + databaseEntity.getTable() + "');");
        statement.executeUpdate(createDeleteQuery(ITEM_TABLE, ITEMSPECID, databaseEntity.getId()
                .toString(), TYPE, databaseEntity.getTable()));
    }

    public static DatabaseBatchOperation deleteItemSpecOperation(DatabaseEntity databaseEntity) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(databaseEntity.getTable(), databaseEntity.getId()));
            executeDeleteLoanable(statement, databaseEntity);
        };
    }

    public static DatabaseBatchOperation deleteMagazine(Magazine magazine) {
        return statement -> {
            statement.executeUpdate(createDeleteQuery(magazine.getTable(), magazine.getId()));
            statement.executeUpdate(createDeleteQuery(ITEM_TABLE, ITEMSPECID, magazine.getId()
                    .toString(), TYPE, magazine.getTable()));
        };
    }

    public static List<String> findAllFromForeignKey(Statement statement, String table, Long key) throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSet rs = statement.executeQuery(createFindByIdQuery(table, MOVIEID, key.toString()));
        while (rs.next()) {
            list.add(rs.getString(COLUMNINDEX));
        }
        return list;
    }

    public static void defaultSaveOperation(Statement statement, ItemSpecification itemSpecification, String attribute, String value)
            throws SQLException {
        ResultSet rs = statement.executeQuery(createFindByAttributeQuery(itemSpecification.getTable(), attribute, value));
        if (rs.next()) {
            itemSpecification.setId(rs.getLong(ID));
        }
        else {
            statement.executeUpdate(createSaveQuery(itemSpecification.getTableWithColumns(), itemSpecification.toSQLValue()));
        }
    }

    public static ItemSpecification getItemSpec(String type, ResultSet rs, Statement statement, Long id) throws SQLException {
        if (type.equalsIgnoreCase(Music.class.getSimpleName())) {
            return Music.buildMusic(rs);
        }
        else if (type.equalsIgnoreCase(Magazine.class.getSimpleName())) {
            return Magazine.buildMagazine(rs);
        }
        else if (type.equalsIgnoreCase(Book.class.getSimpleName())) {
            return Book.buildBook(rs);
        }
        else if (type.equalsIgnoreCase(Movie.class.getSimpleName())) {
            Movie movie = Movie.buildMovie(rs, null, null, null);
            movie.setProducers(findAllFromForeignKey(statement, PRODUCERS_TABLE, id));
            movie.setActors(findAllFromForeignKey(statement, ACTORS_TABLE, id));
            movie.setDubbed(findAllFromForeignKey(statement, DUBBED_TABLE, id));
            return movie;
        }
        return null;
    }
}


