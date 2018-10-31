package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.Movie;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MovieRepository implements Repository<Movie> {

    private final static String MOVIEID = "movieId";
    private final static int COLUMNINDEX = 3;


    @Override
    public void save(Movie movie) {
        executeBatchOperation(ItemSpecificationOperation.movieSaveOperation(movie));
    }

    @Override
    public void saveAll(Movie... movies) {
        UnitOfWork uow = new UnitOfWork();
        for (Movie movie : movies) {
            uow.registerOperation(ItemSpecificationOperation.movieSaveOperation(movie));
        }
        uow.commit();
    }

    @Override
    public void delete(Movie movie) {
        executeUpdate(createDeleteQuery(movie.getTable(), movie.getId()));
        executeUpdate(createDeleteQuery(movie.getProducersTable(), MOVIEID, movie.getId().toString()));
        executeUpdate(createDeleteQuery(movie.getActorsTable(), MOVIEID, movie.getId().toString()));
        executeUpdate(createDeleteQuery(movie.getDubbedTable(), MOVIEID, movie.getId().toString()));
    }

    @Override
    public Movie findById(Long id) {
        return (Movie) executeQuery(createFindByIdQuery(MOVIE_TABLE, id), rs -> {
            if (rs.next()) {
                List<String> producers = findAllFromForeignKey(PRODUCERS_TABLE, id);
                List<String> actors = findAllFromForeignKey(ACTORS_TABLE, id);
                List<String> dubbed = findAllFromForeignKey(DUBBED_TABLE, id);
                return Movie.builder().id(rs.getLong(ID)).actors(actors).date(rs.getString(RELEASEDATE)).director(rs.getString(DIRECTOR))
                        .dubbed(dubbed).lang(rs.getString(LANGUAGE)).producers(producers).runTime(rs.getInt(RUNTIME))
                        .subtitles(rs.getString(SUBTITLES)).title(rs.getString(TITLE)).build();
            }

            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Movie> findAll() {
        return (List<Movie>) executeQueryExpectMultiple(createFindAllQuery(MOVIE_TABLE), rs -> {
            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong(ID);
                List<String> producers = findAllFromForeignKey(PRODUCERS_TABLE, id);
                List<String> actors = findAllFromForeignKey(ACTORS_TABLE, id);
                List<String> dubbed = findAllFromForeignKey(DUBBED_TABLE, id);
                movies.add(Movie.builder().id(id).actors(actors).date(rs.getString(RELEASEDATE)).director(rs.getString(DIRECTOR))
                        .dubbed(dubbed).lang(rs.getString(LANGUAGE)).producers(producers).runTime(rs.getInt(RUNTIME))
                        .subtitles(rs.getString(SUBTITLES)).title(rs.getString(TITLE)).build());
            }

            return movies;
        });
    }

    @Override
    public void update(Movie movie) {
        executeUpdate(createUpdateQuery(movie.getTable(), movie.sqlUpdateValues(), movie.getId()));

        executeUpdate(createDeleteQuery(movie.getProducersTable(), MOVIEID, movie.getId().toString()));
        executeUpdate(createDeleteQuery(movie.getActorsTable(), MOVIEID, movie.getId().toString()));
        executeUpdate(createDeleteQuery(movie.getDubbedTable(), MOVIEID, movie.getId().toString()));
        executeUpdate(createSaveQuery(movie.getProducersTableWithColumns(), movie.getProducersSQLValues()));
        executeUpdate(createSaveQuery(movie.getActorsTableWithColumns(), movie.getActorsSQLValues()));
        executeUpdate(createSaveQuery(movie.getDubbedTableWithColumns(), movie.getDubbedSQLValues()));
    }

    private List<String> findAllFromForeignKey(String table, Long key) {
        List<String> list = new ArrayList<>();
        executeQueryExpectMultiple(createFindByIdQuery(table, MOVIEID, key.toString()), rs -> {
            while (rs.next()) {
                list.add(rs.getString(COLUMNINDEX));
            }
            return null;
        });
        return list;
    }


}
