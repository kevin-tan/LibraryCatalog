package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.catalog.Magazine;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MagazineRepository implements Repository<Magazine> {

    @Override
    public void save(Magazine magazine) {
        executeUpdate(createSaveQuery(magazine.getTableWithColumns(), magazine.toSQLValue()));
    }

    @Override
    public void saveAll(Magazine... magazines) {
        UnitOfWork uow = new UnitOfWork();
        for (Magazine magazine : magazines) {
            uow.registerOperation(statement -> executeUpdate(QueryBuilder.createSaveQuery(magazine.getTableWithColumns(), magazine.toSQLValue())));
        }
        uow.commit();
    }

    @Override
    public void delete(Magazine magazine) {
        executeUpdate(createDeleteQuery(magazine.getTable(), magazine.getId()));
    }

    @Override
    public Magazine findById(Long id) {
        return (Magazine) executeQuery(createFindByIdQuery(MAGAZINE_TABLE, id), rs -> {
            if (rs.next()) {
                return Magazine.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).build();
            }

            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Magazine> findAll() {
        return (List<Magazine>) executeQueryExpectMultiple(createFindAllQuery(MAGAZINE_TABLE), rs -> {
            List<Magazine> magazines = new ArrayList<>();
            while (rs.next()) {
                magazines.add(Magazine.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).build());
            }

            return magazines;
        });
    }

    @Override
    public void update(Magazine magazine) {
        executeUpdate(createUpdateQuery(magazine.getTable(), magazine.sqlUpdateValues(), magazine.getId()));
    }
}
