package com.soen343.project.repository.dao.user;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.dao.Repository;
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
import static com.soen343.project.repository.entity.user.types.UserType.ADMIN;
import static com.soen343.project.repository.entity.user.types.UserType.CLIENT;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Component
public class UserRepository implements Repository<User> {

    @Override
    public void save(User entity) {
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    @Override
    public void saveAll(User... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (User entity : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(QueryBuilder.createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue())));
        }
        uow.commit();
    }

    @Override
    public void delete(User entity) {
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
    }

    @Override
    public User findById(Long id) {
        return (User) executeQuery(createFindByIdQuery(USER_TABLE, id), (rs,statement) -> {
            while (rs.next()) {
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
            }
            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return (List<User>) executeQueryExpectMultiple(createFindAllQuery(USER_TABLE), (rs, statement) -> {
            List<DatabaseEntity> list = new ArrayList<>();
            while (rs.next()) {
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
            }
            return list;
        });
    }

    @Override
    public void update(User entity) {
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }
}
