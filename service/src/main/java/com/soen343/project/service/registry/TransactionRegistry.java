package com.soen343.project.service.registry;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.dao.transaction.ReturnTransactionGateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.transaction.ReturnTransaction;
import com.soen343.project.repository.entity.user.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.soen343.project.repository.entity.EntityConstants.DUEDATE;
import static com.soen343.project.repository.entity.EntityConstants.TRANSACTIONDATE;

@Service
public class TransactionRegistry {

    private static final String LOAN_TRANSACTION = "loanTransaction";
    private static final String RETURN_TRANSACTION = "returnTransaction";

    private final LoanTransactionGateway loanTransactionGateway;
    private final LoanableItemGateway loanableItemGateway;
    private final ReturnTransactionGateway returnTransactionGateway;

    @Autowired
    public TransactionRegistry(LoanTransactionGateway loanTransactionGateway, LoanableItemGateway loanableItemGateway,
                               ReturnTransactionGateway returnTransactionGateway) {
        this.loanTransactionGateway = loanTransactionGateway;
        this.loanableItemGateway = loanableItemGateway;
        this.returnTransactionGateway = returnTransactionGateway;
    }

    public Map<String, List<?>> getAllTransactions() {
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findAll(), RETURN_TRANSACTION, returnTransactionGateway.findAll());
    }

    public ResponseEntity<?> addLoanTransactions(Client client, List<LoanableItem> loanables) {
        // Check for if the loanables are available
        List<LoanableItem> failedLoanedItems = loanableItemGateway.findIfLoanablesAreAvailable(loanables);

        if (failedLoanedItems.isEmpty()) {
            // Create loan transaction
            List<LoanTransaction> transactions = new LinkedList<>();
            loanables.forEach(loanableItem -> transactions.add(new LoanTransaction(loanableItem, client, LocalDateTime.now())));
            // Check + create the loan transaction
            loanTransactionGateway.saveAll(transactions.toArray(new LoanTransaction[]{}));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(failedLoanedItems, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> addReturnTransactions(Client client, List<LoanableItem> loanables) {
        LocalDateTime transactionDate = LocalDateTime.now();
        List<ReturnTransaction> transactions = new LinkedList<>();
        loanables.forEach(loanableItem -> transactions.add(new ReturnTransaction(loanableItem, client, transactionDate)));
        returnTransactionGateway.saveAll(transactions.toArray(new ReturnTransaction[]{}));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<?> searchLoanTransactions() {
        return loanTransactionGateway.findAll();
    }

    public List<?> searchReturnTransactions() {
        return returnTransactionGateway.findAll();
    }

    public Map<String, List<?>> searchAllByTransactionDate(ObjectNode transactionDate) {
        return ImmutableMap
                .of(LOAN_TRANSACTION, loanTransactionGateway.findByTransactionDate(transactionDate.get(TRANSACTIONDATE).asText()),
                        RETURN_TRANSACTION, returnTransactionGateway.findByTransactionDate(transactionDate.get(TRANSACTIONDATE).asText()));
    }

    public List<?> searchLoanByTransactionDate(ObjectNode transactionDate) {
        return loanTransactionGateway.findByTransactionDate(transactionDate.get(TRANSACTIONDATE).asText());
    }

    public List<?> searchLoanByDueDate(ObjectNode dueDate) {
        return loanTransactionGateway.findByDueDate(dueDate.get(DUEDATE).asText());
    }

    public List<?> searchReturnByTransactionDate(ObjectNode transactionDate) {
        return returnTransactionGateway.findByTransactionDate(transactionDate.get(TRANSACTIONDATE).asText());
    }

    public Map<String, List<?>> searchTransactionsByUserId(Long userId) {
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findByUserId(userId), RETURN_TRANSACTION,
                returnTransactionGateway.findByUserId(userId));
    }

    public List<?> searchLoanByUserId(Long userId) {
        return loanTransactionGateway.findByUserId(userId);
    }

    public List<?> searchReturnByUserId(Long userId) {
        return returnTransactionGateway.findByUserId(userId);
    }

    public Map<String, List<?>> searchTransactionByItemType(String itemType) {
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findByItemType(itemType), RETURN_TRANSACTION,
                returnTransactionGateway.findByItemType(itemType));
    }
}
