package com.soen343.project.repository.dao.user;

import com.soen343.project.database.base.DatabaseEntity;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserGateway implements Gateway<User> {

    private final Scheduler scheduler;

    @Autowired
    public UserGateway(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    @Override
    public void save(User entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(User... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (User entity : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(QueryBuilder.createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(User entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();
    }

    @Override
    public User findById(Long id) {
        scheduler.reader_p();
        User user = (User) executeQuery(createFindByIdQuery(USER_TABLE, id), (rs,statement) -> {
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
        scheduler.reader_v();
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        scheduler.reader_p();
        List<User> list = (List<User>) executeQueryExpectMultiple(createFindAllQuery(USER_TABLE), (rs, statement) -> {
            List<DatabaseEntity> tempList = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString(USER_TYPE).equalsIgnoreCase(ADMIN)) {
                    tempList.add(Admin.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build());
                }
                else if (rs.getString(USER_TYPE).equalsIgnoreCase(CLIENT)) {
                    tempList.add(Client.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build());
                }
            }
            return tempList;
        });
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(User entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }
}
