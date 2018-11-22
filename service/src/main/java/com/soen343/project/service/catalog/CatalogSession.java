package com.soen343.project.service.catalog;

import com.soen343.project.repository.concurrency.Scheduler;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import lombok.Data;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.soen343.project.database.query.QueryBuilder.*;
import static com.soen343.project.repository.dao.catalog.item.com.LoanableItemOperation.*;
import static com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation.*;
import static com.soen343.project.repository.entity.EntityConstants.*;

@Data
class CatalogSession {

    private UnitOfWork unitOfWork;
    private final Scheduler scheduler;
    private final AtomicLong specId;
    private final Map<Long, ItemSpecification> itemSpecMap;
    private final Map<Long, Integer> itemSpecCount;

    CatalogSession(Scheduler scheduler, Long latestItemId) {
        this.scheduler = scheduler;
        this.unitOfWork = new UnitOfWork();
        this.specId = new AtomicLong(latestItemId);
        this.itemSpecMap = new HashMap<>();
        this.itemSpecCount = new HashMap<>();
    }

    void updateEntry(ItemSpecification itemSpec) {
        if (itemSpecMap.get(itemSpec.getId()) != null) {
            itemSpecMap.put(itemSpec.getId(), itemSpec);
        } else {
            if (itemSpec.getTable().equals(MOVIE_TABLE)) {
                unitOfWork.registerOperation(ItemSpecificationOperation.movieUpdateOperation((Movie) itemSpec));
            } else {
                unitOfWork.registerOperation(statement -> statement
                        .executeUpdate(createUpdateQuery(itemSpec.getTable(), itemSpec.sqlUpdateValues(), itemSpec.getId())));
            }
        }
    }

    void addEntry(Item item) {
        Long itemSpecId = item.getSpec().getId();
        if (itemSpecCount.get(itemSpecId) != null) {
            itemSpecCount.put(itemSpecId, itemSpecCount.get(itemSpecId) + 1);
        } else {
            if (item.getSpec().getTable().equals(MAGAZINE_TABLE)) {
                unitOfWork.registerOperation(
                        statement -> statement.executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue())));
            } else {
                unitOfWork.registerOperation(saveQuery((LoanableItem) item));
            }
        }
    }

    ItemSpecification addEntry(ItemSpecification itemSpec) {
        if (itemSpec.getId() == 0) {
            Long id = specId.incrementAndGet();
            itemSpec.setId(id);
            itemSpecCount.put(id, 0);
            itemSpecMap.put(id, itemSpec);
        } else {
            if (itemSpec.getTable().equals(MOVIE_TABLE)) {
                unitOfWork.registerOperation(ItemSpecificationOperation.movieSaveOperation((Movie) itemSpec));
            } else {
                unitOfWork.registerOperation(
                        statement -> statement.executeUpdate(createSaveQuery(itemSpec.getTableWithColumns(), itemSpec.toSQLValue())));
            }
        }
        return itemSpec;
    }

    void removeItem(ItemSpecification itemSpec) {
        Long itemSpecId = itemSpec.getId();
        if (itemSpecCount.get(itemSpecId) != null) {
            itemSpecCount.put(itemSpecId, itemSpecCount.get(itemSpecId) - 1);
        } else {
            if (itemSpec.getTable().equals(MAGAZINE_TABLE)) {
                unitOfWork.registerOperation(statement -> {
                    ResultSet rs = statement.executeQuery(
                            "SELECT * FROM " + ITEM_TABLE + " WHERE " + TYPE + " = '" + itemSpec.getTable() + "' and " + ITEMSPECID +
                            " = " + itemSpec.getId() + " LIMIT 1;");
                    rs.next(); // Move to query result
                    statement.executeUpdate(createDeleteQuery(ITEM_TABLE, rs.getLong(ID)));
                });
            } else {
                unitOfWork.registerOperation(statement -> {
                    ResultSet rs = statement.executeQuery(queryLoanableAndItemByItemspecType(itemSpec.getTable(), itemSpec.getId()) +
                                                          " and LoanableItem.available = 1 LIMIT 1;");
                    rs.next();
                    Long id = rs.getLong(ID);
                    statement.execute(createDeleteQuery(ITEM_TABLE, id));
                    statement.execute(createDeleteQuery(LOANABLEITEM_TABLE, id));
                });
            }
        }
    }

    void removeEntry(ItemSpecification itemSpec) {
        if (itemSpecMap.get(itemSpec.getId()) != null) {
            itemSpecMap.remove(itemSpec.getId());
            itemSpecCount.remove(itemSpec.getId());
        } else {
            if (!itemSpec.getTable().equals(MOVIE_TABLE)) {
                if (itemSpec.getTable().equals(MAGAZINE_TABLE)) {
                    unitOfWork.registerOperation(deleteMagazine((Magazine) itemSpec));
                } else {
                    unitOfWork.registerOperation(deleteItemSpecOperation(itemSpec));
                }
            } else {
                unitOfWork.registerOperation(movieDeleteOperation((Movie) itemSpec));
            }
        }
    }

    void editUser(User user) {
        unitOfWork.registerOperation(
                statement -> statement.executeUpdate(createUpdateQuery(user.getTable(), user.sqlUpdateValues(), user.getId())));
    }

    void deleteUser(Long userId) {
        unitOfWork.registerOperation(statement -> statement.executeUpdate(createDeleteQuery(USER_TABLE, userId)));
    }

    void endSession() {
        // Commit maps first
        for (Long key : itemSpecMap.keySet()) {
            unitOfWork.registerOperation(statement -> {
                // Add item spec
                ItemSpecification itemSpec = itemSpecMap.get(key);
                if (itemSpec.getTable().equals(MOVIE_TABLE)) {
                    movieSaveOperation((Movie) itemSpec);
                } else {
                    statement.executeUpdate(createSaveQuery(itemSpec.getTableWithColumns(), itemSpec.toSQLValue()));
                }

                // Get new ID for item spec
                ResultSet itemSpecRS = statement.executeQuery(createFindIdOfLastAddedItem(itemSpec.getTable()));
                itemSpec.setId(itemSpecRS.getLong(ID));

                // Add item/loanableItem count
                for (int i = 0; i < itemSpecCount.get(key); i++) {
                    if (itemSpec.getTable().equals(MAGAZINE_TABLE)) {
                        Item item = new Item(itemSpec);
                        statement.executeUpdate(createSaveQuery(item.getTableWithColumns(), item.toSQLValue()));
                    } else {
                        loanableSaveOperation(statement, new LoanableItem(itemSpec));
                    }
                }
            });
        }
        scheduler.writer_p();
        unitOfWork.commit();
        scheduler.writer_v();
    }
}
