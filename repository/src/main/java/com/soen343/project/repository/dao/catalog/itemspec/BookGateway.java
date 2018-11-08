package com.soen343.project.repository.dao.catalog.itemspec;

import com.soen343.project.database.connection.operation.DatabaseQueryOperation;
import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
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
@SuppressWarnings("ALL")
public class BookGateway implements Gateway<Book> {

    private final Scheduler scheduler;

    @Autowired
    public BookGateway(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    @Override
    public void save(Book book) {
        scheduler.writer_p();
        executeBatchUpdate(statement -> defaultSaveOperation(statement, book, ISBN10, book.getIsbn10()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Book... books) {
        UnitOfWork uow = new UnitOfWork();
        for (Book book : books) {
            uow.registerOperation(statement -> defaultSaveOperation(statement, book, ISBN10, book.getIsbn10()));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Book book) {
        scheduler.writer_p();
        executeBatchUpdate(ItemSpecificationOperation.bookDeleteOperation(book));
        scheduler.writer_v();
    }

    @Override
    public Book findById(Long id) {
        scheduler.reader_p();
        Book book = (Book) executeQuery(createFindByIdQuery(BOOK_TABLE, id), (rs, statement) -> {
            if (rs.next()) {
                return Book.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).author(rs.getString(AUTHOR)).pages(rs.getInt(PAGES))
                        .format(Book.Format.stringToEnum(rs.getString(FORMAT))).build();
            }

            //Should never be reached
            return null;
        });
        scheduler.reader_v();
        return book;
    }

    public List<Book> findByAttribute(String attribute, String attributeValue) {
        scheduler.reader_p();
        List<Book> list = (List<Book>) executeQueryExpectMultiple(createSearchByAttributeQuery(BOOK_TABLE, attribute, attributeValue), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> findAll() {
        scheduler.reader_p();
        List<Book> list = (List<Book>) executeQueryExpectMultiple(createFindAllQuery(BOOK_TABLE), databaseQueryOperation());
        scheduler.reader_v();
        return list;
    }

    @Override
    public void update(Book book) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(book.getTable(), book.sqlUpdateValues(), book.getId()));
        scheduler.writer_v();
    }

    private DatabaseQueryOperation databaseQueryOperation(){
        return (rs, statement) -> {
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(Book.builder().id(rs.getLong(ID)).title(rs.getString(TITLE)).isbn10(rs.getString(ISBN10))
                        .isbn13(rs.getString(ISBN13)).lang(rs.getString(LANGUAGE)).pubDate(rs.getString(PUBDATE))
                        .publisher(rs.getString(PUBLISHER)).author(rs.getString(AUTHOR)).pages(rs.getInt(PAGES))
                        .format(Book.Format.stringToEnum(rs.getString(FORMAT))).build());
            }

            return books;
        };
    }
}
