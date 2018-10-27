package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.catalog.Book;
import com.soen343.project.repository.entity.catalog.Magazine;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class BookRepository implements Repository<Book> {

    @Override
    public void save(Book book) {
        executeUpdate(createSaveQuery(book.getTableWithColumns(), book.toSQLValue()));
    }

    @Override
    public void saveAll(Book... books) {
        UnitOfWork<Book> uow = new UnitOfWork<>();
        for (Book book : books) {
            uow.registerCreate(book);
        }
        uow.commit();
    }

    @Override
    public void delete(Book book) {
        executeUpdate(createDeleteQuery(book.getTable(), book.getId()));
    }

    @Override
    public Book findById(Long id) {
        return (Book) executeQuery(createFindByIdQuery(BOOK_TABLE, id), rs -> {
            if (rs.next()) {
                return Book.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).author(rs.getString(AUTHOR)).pages(rs.getInt(PAGES))
                        .format(Book.Format.stringToEnum(rs.getString(FORMAT))).build();
            }

            //Should never be reached
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> findAll() {
        return (List<Book>) executeQueryExpectMultiple(createFindAllQuery(BOOK_TABLE), rs -> {
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(Book.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).author(rs.getString(AUTHOR)).pages(rs.getInt(PAGES))
                        .format(Book.Format.stringToEnum(rs.getString(FORMAT))).build());
            }

            return books;
        });
    }

    @Override
    public void update(Book book) {
        executeUpdate(createUpdateQuery(book.getTable(), book.sqlUpdateValues(), book.getId()));
    }
}
