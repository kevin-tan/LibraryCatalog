package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.catalog.itemspec.com.ItemSpecificationGateway;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
@SuppressWarnings("ALL")
public class MovieGateway implements ItemSpecificationGateway<Movie> {

    private final Scheduler scheduler;

    @Autowired
    public MovieGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Movie movie) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.movieSaveOperation(movie));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Movie... movies) {
        UnitOfWork uow = new UnitOfWork();
        for (Movie movie : movies) {
            uow.registerOperation(ItemSpecificationOperation.movieSaveOperation(movie));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Movie movie) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.movieDeleteOperation(movie));
        scheduler.writer_v();
    }

    @Override
    public Movie findById(Long id) {
        scheduler.reader_p();
        Movie movie = (Movie) exectuteBatchQuery(statement -> {
            // Get Movie Id
            ResultSet rs = statement.executeQuery(createFindByIdQuery(MOVIE_TABLE, id));

            // Build movie
            Movie m = Movie.builder().id(rs.getLong(ID)).actors(null).date(rs.getString(RELEASEDATE)).director(rs.getString(DIRECTOR))
                    .dubbed(null).lang(rs.getString(LANGUAGE)).producers(null).runTime(rs.getInt(RUNTIME))
                    .subtitles(rs.getString(SUBTITLES)).title(rs.getString(TITLE)).build();

            //Only single id for rs, thus no need to check rs.next()
            List<String> producers = findAllFromForeignKey(statement, PRODUCERS_TABLE, id);
            List<String> actors = findAllFromForeignKey(statement, ACTORS_TABLE, id);
            List<String> dubbed = findAllFromForeignKey(statement, DUBBED_TABLE, id);

            m.setProducers(producers);
            m.setActors(actors);
            m.setDubbed(dubbed);
            return m;
        });
        scheduler.reader_v();
        return movie;
    }

    //TODO Bugged, does not search the foreign key
    @Override
    public List<Movie> findByAttribute(Map<String, String> attributeValue) {
        scheduler.reader_p();
        List<Movie> list = (List<Movie>) executeQueryExpectMultiple(createSearchByAttributesQuery(MOVIE_TABLE, attributeValue),
                databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    public List<Movie> findAll() {
        scheduler.reader_p();
        List<Movie> list = (List<Movie>) executeQueryExpectMultiple(createFindAllQuery(MOVIE_TABLE), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Movie movie) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.movieUpdateOperation(movie));
        scheduler.writer_v();
    }

    @Override
    public List<Movie> findByTitle(String title) {
        scheduler.reader_p();
        List<Movie> list =
                (List<Movie>) executeQueryExpectMultiple(createSearchByAttributeQuery(MOVIE_TABLE, TITLE, title), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    private DatabaseQueryOperation databaseQueryOperation() {
        return (rs, statement) -> {
            List<Movie> movies = new LinkedList<>();
            while (rs.next()) {
                movies.add(Movie.buildMovie(rs, null, null, null));
            }

            for (Movie movie : movies) {
                movie.setProducers(findAllFromForeignKey(statement, PRODUCERS_TABLE, movie.getId()));
                movie.setActors(findAllFromForeignKey(statement, ACTORS_TABLE, movie.getId()));
                movie.setDubbed(findAllFromForeignKey(statement, DUBBED_TABLE, movie.getId()));
            }
            return movies;
        };
    }
}
