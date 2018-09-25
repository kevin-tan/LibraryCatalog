package com.soen343.project.repository.dao.user;

import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;

import static com.soen343.project.database.connection.DatabaseConnector.executeQuery;
import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.EntityConstants.*;
import static com.soen343.project.repository.entity.user.types.UserType.ADMIN;
import static com.soen343.project.repository.entity.user.types.UserType.CLIENT;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class UserRepository implements Repository<User> {

    @Override
    public synchronized void save(User entity) {
        executeUpdate(createSaveQuery(USER_TABLE_WITH_COLUMNS, entity.toSQLValue()));
    }

    @Override
    public synchronized void saveAll(User... entities) {
        //TODO Cleanup to execute single query instead of multiple
        for (User entity : entities) {
            save(entity);
        }
    }

    @Override
    public synchronized void delete(User entity) {
        executeUpdate(createDeleteQuery(USER_TABLE, entity.getId()));
    }

    @Override
    public User find(Long id) {

        return (User) executeQuery(createFindByIdQuery(USER_TABLE, id), rs -> {
            while (rs.next()) {
                if (rs.getString(USER_TYPE).equalsIgnoreCase(ADMIN)) {
                    return Admin.builder()
                            .id(rs.getLong(ID))
                            .firstName(rs.getString(FIRST_NAME))
                            .lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL))
                            .phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS))
                            .build();
                }
                else if (rs.getString(USER_TYPE).equalsIgnoreCase(CLIENT)) {
                    return Client.builder()
                            .id(rs.getLong(ID))
                            .firstName(rs.getString(FIRST_NAME))
                            .lastName(rs.getString(LAST_NAME))
                            .email(rs.getString(EMAIL))
                            .phoneNumber(rs.getString(PHONE_NUMBER))
                            .physicalAddress(rs.getString(PHYSICAL_ADDRESS))
                            .build();
                }
            }
            //Should never be reached
            return null;
        });
    }

    @Override
    public synchronized void update(User entity) {
        executeUpdate(createUpdateQuery(USER_TABLE, entity.sqlUpdateValues(), entity.getId()));
    }
}
