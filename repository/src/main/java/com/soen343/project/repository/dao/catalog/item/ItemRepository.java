package com.soen343.project.repository.dao.catalog.item;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;
import static com.soen343.project.repository.entity.user.types.UserType.ADMIN;
import static com.soen343.project.repository.entity.user.types.UserType.CLIENT;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Component
public class ItemRepository implements Repository<Item> {

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
                //return Item.builder().id(rs.getLong(ID)).spec(new ItemSpecification(rs.getLong(ITEMSPECID))).build();
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
                list.add(Item.builder().id(rs.getLong(ID)).build());

            }
            return list;
        });
    }

    @Override
    public void update(Item entity) {
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }
}
