package com.soen343.project.repository.dao.loanable;

import com.soen343.project.repository.dao.Repository;
import com.soen343.project.repository.entity.loanable.Loan;
import com.soen343.project.repository.uow.UnitOfWork;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.soen343.project.database.connection.DatabaseConnector.*;
import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.entity.EntityConstants.*;


public class LoanableRepository implements Repository<Loan> {

    @Override
    public void save(Loan entity) {
        executeUpdate(createSaveQuery(entity.getTableWithColumns(), entity.toSQLValue()));
    }

    @Override
    public void saveAll(Loan... entities) {
        UnitOfWork uow = new UnitOfWork();
        for (Loan loan : entities) {
            uow.registerOperation(
                    statement -> executeUpdate(createSaveQuery(loan.getTableWithColumns(), loan.toSQLValue())));
        }
        uow.commit();
    }

    @Override
    public void delete(Loan entity) {
        executeUpdate(createDeleteQuery(entity.getTable(), entity.getId()));
    }

    @Override
    public Loan findById(Long id) {
        return (Loan) executeQuery(createFindByIdQuery(LOAN_TABLE, id), (rs, statement) -> {
            long itemID = rs.getLong(ITEMID);
            ResultSet itemRS = statement.executeQuery(createFindByIdQuery(ITEM_TABLE, itemID));
            if (rs.next()) {

                return Loan.builder().id(rs.getLong(ID)).item(rs.getItem(ITEMID)).loanTime(rs.getString(LOANTIME))
                        .checkoutDate(rs.getString(CHECKOUTDATE)).dueDate(rs.getString(DUEDATE)).build();

            }
            return null;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Loan> findAll() {
        return (List<Loan>) executeQueryExpectMultiple(createFindAllQuery(LOAN_TABLE), (rs, statement) -> {
            List<Loan> loans = new ArrayList<>();

            while (rs.next()) {
                loans.add(Loan.builder().id(rs.getLong(ID)).item(rs.getLong(ITEMID)).loanTime(rs.getString(LOANTIME))
                        .checkoutDate(rs.getString(CHECKOUTDATE)).dueDate(rs.getString(DUEDATE)).build());
            }

            return loans;
        });
    }

    @Override
    public void update(Loan entity) {
        executeUpdate(createUpdateQuery(entity.getTable(), entity.sqlUpdateValues(), entity.getId()));
    }


}
