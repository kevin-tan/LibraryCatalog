package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.catalog.Movie;
import com.soen343.project.repository.entity.catalog.Music;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MusicRepository implements Repository<Music> {

    @Override
    public void save(Music music) {
        executeUpdate(createSaveQuery(music.getTableWithColumns(), music.toSQLValue()));
    }

    @Override
    public void saveAll(Music... musics) {
        UnitOfWork<Music> uow = new UnitOfWork<>();
        for (Music music : musics) {
            uow.registerCreate(music);
        }
        uow.commit();
    }

    @Override
    public void delete(Music music) {
        executeUpdate(createDeleteQuery(music.getTable(), music.getId()));
    }

    @Override
    public Music findById(Long id) {
        return (Music) executeQuery(createFindByIdQuery(MUSIC_TABLE, id), rs -> {
            if (rs.next()) {
                return Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                        .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL))
                        .type(rs.getString(TYPE)).build();
            }

            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Music> findAll() {
        return (List<Music>) executeQueryExpectMultiple(createFindAllQuery(MUSIC_TABLE), rs -> {
            List<Music> musics = new ArrayList<>();
            while (rs.next()) {
                musics.add(Music.builder().id(rs.getLong(ID)).date(rs.getString(RELEASEDATE)).title(rs.getString(TITLE))
                        .artist(rs.getString(ARTIST)).asin(rs.getString(ASIN)).label(rs.getString(LABEL))
                        .type(rs.getString(TYPE)).build());
            }

            return musics;
        });
    }

    @Override
    public void update(Music music) {
        executeUpdate(createUpdateQuery(music.getTable(), music.sqlUpdateValues(), music.getId()));
    }
}
