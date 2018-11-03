package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.defaultSaveOperation;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MusicRepository implements Repository<Music> {

    private final Scheduler scheduler;

    @Autowired
    public MusicRepository(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Music music) {
        scheduler.addWriter();
        executeBatchUpdate(statement -> defaultSaveOperation(statement, music, ASIN, music.getAsin()));
        scheduler.removeWriter();
    }

    @Override
    public void saveAll(Music... musics) {
        UnitOfWork uow = new UnitOfWork();
        for (Music music : musics) {
            uow.registerOperation(statement -> defaultSaveOperation(statement, music, ASIN, music.getAsin()));
        }
        uow.commit();
    }

    @Override
    public void delete(Music music) {
        executeBatchUpdate(ItemSpecificationOperation.musicDeleteOperation(music));
    }

    @Override
    public Music findById(Long id) {

        return (Music) executeQuery(createFindByIdQuery(MUSIC_TABLE, id), (rs, statement) -> {
            if (rs.next()) {
                return Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                        .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL)).type(rs.getString(TYPE)).build();
            }
            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Music> findAll() {
        return (List<Music>) executeQueryExpectMultiple(createFindAllQuery(MUSIC_TABLE), (rs, statement) -> {
            List<Music> musics = new ArrayList<>();
            while (rs.next()) {
                musics.add(Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                        .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL)).type(rs.getString(TYPE)).build());
            }

            return musics;
        });
    }

    @Override
    public void update(Music music) {
        executeUpdate(createUpdateQuery(music.getTable(), music.sqlUpdateValues(), music.getId()));
    }
}
