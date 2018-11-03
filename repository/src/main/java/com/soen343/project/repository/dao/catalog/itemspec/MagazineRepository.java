package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.defaultSaveOperation;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MagazineRepository implements Repository<Magazine> {

    @Override
    public void save(Magazine magazine) {
        executeBatchUpdate(statement -> defaultSaveOperation(statement, magazine, ISBN10, magazine.getIsbn10()));
    }

    @Override
    public void saveAll(Magazine... magazines) {
        UnitOfWork uow = new UnitOfWork();
        for (Magazine magazine : magazines) {
            uow.registerOperation(statement -> defaultSaveOperation(statement, magazine, ISBN10, magazine.getIsbn10()));
        }
        uow.commit();
    }

    @Override
    public void delete(Magazine magazine) {
        executeBatchUpdate(ItemSpecificationOperation.magazineDeleteOperation(magazine));
    }

    @Override
    public Magazine findById(Long id) {
        return (Magazine) executeQuery(createFindByIdQuery(MAGAZINE_TABLE, id), (rs, statement) -> {
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
        return (List<Magazine>) executeQueryExpectMultiple(createFindAllQuery(MAGAZINE_TABLE), (rs, statement) -> {
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
