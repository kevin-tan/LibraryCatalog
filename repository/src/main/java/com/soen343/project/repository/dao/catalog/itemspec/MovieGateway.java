package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.createFindAllQuery;
import static com.soen343.project.database.query.QueryBuilder.createFindByIdQuery;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MovieGateway implements Gateway<Movie> {

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

            //Only single id for rs, thus no need to check rs.next()
            List<String> producers = findAllFromForeignKey(statement, PRODUCERS_TABLE, id);
            List<String> actors = findAllFromForeignKey(statement, ACTORS_TABLE, id);
            List<String> dubbed = findAllFromForeignKey(statement, DUBBED_TABLE, id);

            // Build movie
            return Movie.builder().id(rs.getLong(ID)).actors(actors).date(rs.getString(RELEASEDATE)).director(rs.getString(DIRECTOR))
                    .dubbed(dubbed).lang(rs.getString(LANGUAGE)).producers(producers).runTime(rs.getInt(RUNTIME))
                    .subtitles(rs.getString(SUBTITLES)).title(rs.getString(TITLE)).build();
        });
        scheduler.reader_v();
        return movie;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Movie> findAll() {
        scheduler.reader_p();
        List<Movie> list = (List<Movie>) executeQueryExpectMultiple(createFindAllQuery(MOVIE_TABLE), (rs, statement) -> {
            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong(ID);
                List<String> producers = findAllFromForeignKey(statement, PRODUCERS_TABLE, id);
                List<String> actors = findAllFromForeignKey(statement, ACTORS_TABLE, id);
                List<String> dubbed = findAllFromForeignKey(statement, DUBBED_TABLE, id);
                movies.add(Movie.builder().id(id).actors(actors).date(rs.getString(RELEASEDATE)).director(rs.getString(DIRECTOR))
                        .dubbed(dubbed).lang(rs.getString(LANGUAGE)).producers(producers).runTime(rs.getInt(RUNTIME))
                        .subtitles(rs.getString(SUBTITLES)).title(rs.getString(TITLE)).build());
            }

            return movies;
        });
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Movie movie) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.movieUpdateOperation(movie));
        scheduler.writer_v();
    }
}