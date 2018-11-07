package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.defaultSaveOperation;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class MagazineGateway implements Gateway<Magazine> {

    private final Scheduler scheduler;

    @Autowired
    public MagazineGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Magazine magazine) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> defaultSaveOperation(statement, magazine, ISBN10, magazine.getIsbn10()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Magazine... magazines) {
        UnitOfWork uow = new UnitOfWork();
        for (Magazine magazine : magazines) {
            uow.registerOperation(statement -> defaultSaveOperation(statement, magazine, ISBN10, magazine.getIsbn10()));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Magazine magazine) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.magazineDeleteOperation(magazine));
        scheduler.writer_v();
    }

    @Override
    public Magazine findById(Long id) {
        scheduler.reader_p();
        Magazine magazine = (Magazine) executeQuery(createFindByIdQuery(MAGAZINE_TABLE, id), (rs, statement) -> {
            if (rs.next()) {
                return Magazine.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).build();
            }

            //Should never be reached
            return null;
        });
        scheduler.reader_v();
        return magazine;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Magazine> findAll() {
        scheduler.reader_p();
        List<Magazine> list = (List<Magazine>) executeQueryExpectMultiple(createFindAllQuery(MAGAZINE_TABLE), (rs, statement) -> {
            List<Magazine> magazines = new ArrayList<>();
            while (rs.next()) {
                magazines.add(Magazine.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).build());
            }

            return magazines;
        });
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Magazine magazine) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(magazine.getTable(), magazine.sqlUpdateValues(), magazine.getId()));
        scheduler.writer_v();
    }
}
