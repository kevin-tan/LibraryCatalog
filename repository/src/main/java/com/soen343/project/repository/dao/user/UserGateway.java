package com.soen343.project.repository.dao.user;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.*;
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
    public UserGateway(Scheduler scheduler) {
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
                    statement -> statement.executeUpdate(QueryBuilder.createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue())));
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
        User user = executeQuery(createFindByIdQuery(USER_TABLE, id), extractUser());
        scheduler.reader_v();
        return user;
    }

    @Override
    public List<User> findByAttribute(Map<String, String> attributeValue) {
        //Not required at the moment
        return null;
    }

    @Override
    public List<User> findAll() {
        scheduler.reader_p();
        List<User> list = executeQueryExpectMultiple(createFindAllQuery(USER_TABLE), (rs, statement) -> {
            List<User> tempList = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString(USER_TYPE).equalsIgnoreCase(ADMIN)) {
                    tempList.add(Admin.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS)).build());
                } else if (rs.getString(USER_TYPE).equalsIgnoreCase(CLIENT)) {
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

    public List<User> findAllAdmins() {
        scheduler.reader_p();
        List<User> user = executeQuery(createSearchByAttributeQuery(USER_TABLE, USER_TYPE, ADMIN), extractAllUser());
        scheduler.reader_v();
        return user;
    }

    public User findByEmail(String email) {
        scheduler.reader_p();
        User user = executeQuery(createSearchByAttributeQuery(USER_TABLE, EMAIL, email), extractUser());
        scheduler.reader_v();
        return user;
    }

    @Override
    public void update(User entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    private DatabaseQueryOperation<User> extractUser() {
        return (rs, statement) -> {
            if (rs.next()) {
                return getUser(rs);
            }
            //Should never be reached
            return null;
        };
    }

    private User getUser(ResultSet rs) throws SQLException {
        if (rs.getString(USER_TYPE).equalsIgnoreCase(ADMIN)) {
            return Admin.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                    .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER)).physicalAddress(rs.getString(PHYSICAL_ADDRESS))
                    .build();
        } else if (rs.getString(USER_TYPE).equalsIgnoreCase(CLIENT)) {
            return Client.builder().id(rs.getLong(ID)).firstName(rs.getString(FIRST_NAME)).lastName(rs.getString(LAST_NAME))
                    .email(rs.getString(EMAIL)).phoneNumber(rs.getString(PHONE_NUMBER)).physicalAddress(rs.getString(PHYSICAL_ADDRESS))
                    .build();
        }
        return null;
    }

    private DatabaseQueryOperation<List<User>> extractAllUser() {
        return (rs, statement) -> {
            List<User> tempList = new ArrayList<>();
            tempList.add(getUser(rs));
            return tempList;
        };
    }

    public User findByPhoneNumber(String phoneNumber) {
        scheduler.reader_p();
        User user = executeQuery(createSearchByAttributeQuery(USER_TABLE, PHONE_NUMBER, phoneNumber), extractUser());
        scheduler.reader_v();
        return user;
    }
}
