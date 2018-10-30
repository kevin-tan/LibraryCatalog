package com.soen343.project.repository.dao.catalog.item;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.dao.catalog.itemspec.BookRepository;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineRepository;
import com.soen343.project.repository.dao.catalog.itemspec.MovieRepository;
import com.soen343.project.repository.dao.catalog.itemspec.MusicRepository;
import com.soen343.project.repository.entity.catalog.*;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;
import static com.soen343.project.repository.entity.user.types.UserType.ADMIN;
import static com.soen343.project.repository.entity.user.types.UserType.CLIENT;

@Component
public class ItemRepository implements Repository<Item> {

    private final MusicRepository musicRepository;
    private final MagazineRepository magazineRepository;
    private final BookRepository bookRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ItemRepository(MusicRepository musicRepository, MagazineRepository magazineRepository,
                          BookRepository bookRepository, MovieRepository movieRepository){
        this.musicRepository = musicRepository;
        this.magazineRepository = magazineRepository;
        this.bookRepository = bookRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public void save(Item entity) {
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    @Override
    public void saveAll(Item... entities) {
        UnitOfWork<Item> uow = new UnitOfWork<>();
        for (Item entity : entities) {
            uow.registerCreate(entity);
        }
        uow.commit();
    }

    @Override
    public void delete(Item entity) {
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
    }

    //TODO
    @Override
    public Item findById(Long id) {
        return (Item) executeQuery(createFindByIdQuery(ITEM_TABLE, id), rs -> {
            if (rs.next()) {
                ItemSpecification itemSpec = getItemSpec(rs.getLong(ITEMSPECID), rs.getString(TYPE));
                return new Item(rs.getLong(ID), itemSpec);
            }
            //Should never be reached
            return null;
        });
    }

    //TODO
    @Override
    @SuppressWarnings("unchecked")
    public List<Item> findAll() {
        return (List<Item>) executeQueryExpectMultiple(createFindAllQuery(ITEM_TABLE), rs -> {
            List<DatabaseEntity> list = new ArrayList<>();
            while (rs.next()) {
                ItemSpecification itemSpec = getItemSpec(rs.getLong(ITEMSPECID), rs.getString(TYPE));
                list.add(new Item(rs.getLong(ID), itemSpec));
            }
            return list;
        });
    }

    @Override
    public void update(Item entity) {
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }

    private ItemSpecification getItemSpec(Long id, String type){


        if (type.equalsIgnoreCase(Music.class.getSimpleName())){
            return musicRepository.findById(id);
        } else if (type.equalsIgnoreCase(Magazine.class.getSimpleName())){
            return magazineRepository.findById(id);
        } else if (type.equalsIgnoreCase(Book.class.getSimpleName())){
            return bookRepository.findById(id);
        } else if (type.equalsIgnoreCase(Movie.class.getSimpleName())){
            return movieRepository.findById(id);
        }

        //Should not be reached
        return null;
    }
}
