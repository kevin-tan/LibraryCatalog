package com.soen343.project.repository.dao.catalog;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.executeQuery;
import static com.soen343.project.database.connection.DatabaseConnector.executeQueryExpectMultiple;
import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class CatalogRepository implements Repository<ItemSpecification> {

    @Override
    public void save(ItemSpecification entity) {
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    @Override
    public void saveAll(ItemSpecification... entities) {
        UnitOfWork<ItemSpecification> uow = new UnitOfWork<>();
        for (ItemSpecification entity : entities) {
            uow.registerCreate(entity);
        }
        uow.commit();
    }

    @Override
    public void delete(ItemSpecification entity) {
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
    }

    @Override
    public ItemSpecification findById(Long id) {
        return (ItemSpecification) executeQuery(createFindByIdQuery(USER_TABLE, id), rs -> {
            /*while (rs.next()) {
                if (rs.getString(USER_TYPE).equalsIgnoreCase(ADMIN)) {
                    return Admin.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build();
                }
                else if (rs.getString(USER_TYPE).equalsIgnoreCase(CLIENT)) {
                    return Client.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build();
                }
            }*/
            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ItemSpecification> findAll() {
        return (List<ItemSpecification>) executeQueryExpectMultiple(createFindAllQuery(USER_TABLE), rs -> {
            List<DatabaseEntity> list = new ArrayList<>();
            /*while (rs.next()) {
                if (rs.getString(USER_TYPE).equalsIgnoreCase(ADMIN)) {
                    list.add(Admin.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build());
                }
                else if (rs.getString(USER_TYPE).equalsIgnoreCase(CLIENT)) {
                    list.add(Client.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build());
                }
            }*/
            return list;
        });
    }

    @Override
    public void update(ItemSpecification entity) {
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }
}
