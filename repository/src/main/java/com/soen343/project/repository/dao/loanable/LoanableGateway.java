package com.soen343.project.repository.dao.loanable;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.loanable.Loan;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.executeForeignKeyTableQuery;
import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.findAllFromForeignKey;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Component
public class LoanableGateway implements Gateway<Loan> {

    private final Scheduler scheduler;

    @Autowired
    public LoanableGateway(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void save(Loan entity) {
        scheduler.writer_p();
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
        scheduler.writer_v();
    }

    @Override
    public void saveAll(Loan... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (Loan loan : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(createSaveQuery(loan.getTableWithColumns(), loan.toSQLValue())));
        }
        scheduler.writer_p();
        uow.commit();
        scheduler.writer_v();
    }

    @Override
    public void delete(Loan entity) {
        scheduler.writer_p();
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
        scheduler.writer_v();

    }

    @Override
    public Loan findById(Long id) {

        scheduler.reader_p();
        Loan loan = (Loan) executeForeignKeyTableQuery(createFindByIdQuery(LOAN_TABLE, id), (rs, statement) -> {
            rs.next();// Move to query result
            Long loanId = rs.getLong(ID);
            Long itemId = rs.getLong(ITEMID);
            Date checkoutDate = rs.getDate(CHECKOUTDATE);
            Date dueDate = rs.getDate(DUEDATE);

            rs.next();
            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, itemId));
            itemRS.next();
            Long itemSpecId = itemRS.getLong(ITEMSPECID);
            String itemSpecType = itemRS.getString(TYPE);

            itemRS.next();
            ResultSet itemSpecRS = statement.executeQuery(createFindByIdQuery(itemSpecType, itemSpecId));
            ItemSpecification itemSpecification = getItemSpec(itemSpecType, itemSpecRS, statement, itemSpecId);

            Item item = new Item(itemId, itemSpecification);

            return new Loan(loanId, item, checkoutDate, dueDate);

        });
        scheduler.reader_v();
        return loan;
    }

    @Override
    public List<Loan> findByAttribute(Map<String, String> attributeValue) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Loan> findAll() {
//        return (List<Loan>) executeQueryExpectMultiple(createFindAllQuery(LOAN_TABLE), (rs, statement) -> {
//            List<Loan> loans = new ArrayList<>();
//
//            while (rs.next()) {
//
//                loans.add(Loan.builder().id(rs.getLong(ID)).item(getItem(rs.getLong(ITEMID))).loanTime(rs.getString(LOANTIME))
//                        .checkoutDate(rs.getString(CHECKOUTDATE)).dueDate(rs.getString(DUEDATE)).build());
//            }
//
//            return loans;
//        });
        return null;
    }

    @Override
    public void update(Loan entity) {
        scheduler.writer_p();
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
        scheduler.writer_v();
    }

    private ItemSpecification getItemSpec(String type, ResultSet rs, Statement statement, Long id) throws SQLException {
        if (type.equalsIgnoreCase(Music.class.getSimpleName())) {
            return Music.buildMusic(rs);
        } else if (type.equalsIgnoreCase(Book.class.getSimpleName())) {
            return Book.buildBook(rs);
        } else if (type.equalsIgnoreCase(Movie.class.getSimpleName())) {
            Movie movie = Movie.buildMovie(rs, null, null, null);
            movie.setProducers(findAllFromForeignKey(statement, PRODUCERS_TABLE, id));
            movie.setActors(findAllFromForeignKey(statement, ACTORS_TABLE, id));
            movie.setDubbed(findAllFromForeignKey(statement, DUBBED_TABLE, id));
            return movie;
        }
        return null;
    }
}
