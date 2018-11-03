package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.defaultSaveOperation;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class BookRepository implements Repository<Book> {

    @Override
    public void save(Book book) {
        executeBatchUpdate(statement -> defaultSaveOperation(statement, book, ISBN10, book.getIsbn10()));
    }

    @Override
    public void saveAll(Book... books) {
        UnitOfWork uow = new UnitOfWork();
        for (Book book : books) {
            uow.registerOperation(statement -> defaultSaveOperation(statement, book, ISBN10, book.getIsbn10()));
        }
        uow.commit();
    }

    @Override
    public void delete(Book book) {
        executeBatchUpdate(ItemSpecificationOperation.bookDeleteOperation(book));
    }

    @Override
    public Book findById(Long id) {
        return (Book) executeQuery(createFindByIdQuery(BOOK_TABLE, id), (rs, statement) -> {
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
        return (List<Book>) executeQueryExpectMultiple(createFindAllQuery(BOOK_TABLE), (rs, statement) -> {
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
