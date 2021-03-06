package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.catalog.itemspec.com.ItemSpecificationGateway;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.defaultSaveOperation;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MusicGateway implements ItemSpecificationGateway<Music> {

    private final Scheduler scheduler;

    @Autowired
    public MusicGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Music music) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> defaultSaveOperation(statement, music, ASIN, music.getAsin()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Music... musics) {
        UnitOfWork uow = new UnitOfWork();
        for (Music music : musics) {
            uow.registerOperation(statement -> defaultSaveOperation(statement, music, ASIN, music.getAsin()));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Music music) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.deleteItemSpecOperation(music));
        scheduler.writer_v();
    }

    @Override
    public Music findById(Long id) {
        scheduler.reader_p();
        Music music = executeQuery(createFindByIdQuery(MUSIC_TABLE, id), (rs, statement) -> {
            if (rs.next()) {
                return Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                        .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL)).type(rs.getString(TYPE)).build();
            }
            //Should never be reached
            return null;
        });
        scheduler.reader_v();
        return music;
    }

    @Override
    public List<Music> findByAttribute(Map<String, String> attributeValue) {
        scheduler.reader_p();
        List<Music> list = executeQueryExpectMultiple(createSearchByAttributesQuery(MUSIC_TABLE, attributeValue), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Music> findAll() {
        scheduler.reader_p();
        List<Music> list = executeQueryExpectMultiple(createFindAllQuery(MUSIC_TABLE), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Music music) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(music.getTable(), music.sqlUpdateValues(), music.getId()));
        scheduler.writer_v();
    }

    private DatabaseQueryOperation databaseQueryOperation() {
        return (rs, statement) -> {
            List<Music> musics = new ArrayList<>();
            while (rs.next()) {
                musics.add(Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                        .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL)).type(rs.getString(TYPE)).build());
            }

            return musics;
        };
    }

    @Override
    public List<Music> findByTitle(String title) {
        scheduler.reader_p();
        List<Music> list = executeQueryExpectMultiple(createSearchByAttributeQuery(MUSIC_TABLE, TITLE, title), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }
}
