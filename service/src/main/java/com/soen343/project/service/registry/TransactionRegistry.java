package com.soen343.project.service.registry;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

import static com.soen343.project.repository.dao.transaction.com.DateConverter.DATE_FORMAT;
import static com.soen343.project.repository.entity.EntityConstants.DUEDATE;
import static com.soen343.project.repository.entity.EntityConstants.TRANSACTIONDATE;

@Service
public class TransactionRegistry {

    private static final String LOAN_TRANSACTION = "loanTransaction";
    private static final String RETURN_TRANSACTION = "returnTransaction";

    private final LoanTransactionGateway loanTransactionGateway;
    private final ReturnTransactionGateway returnTransactionGateway;

    @Autowired
    public TransactionRegistry(LoanTransactionGateway loanTransactionGateway, ReturnTransactionGateway returnTransactionGateway) {
        this.loanTransactionGateway = loanTransactionGateway;
        this.returnTransactionGateway = returnTransactionGateway;
    }

    public Map<String, List<?>> getAllTransactions() {
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findAll(), RETURN_TRANSACTION, returnTransactionGateway.findAll());
    }

    public ResponseEntity<?> addLoanTransactions(Client client, List<LoanableItem> loanables) {
        LocalDateTime transactionDate = LocalDateTime.now();
        String transactionDateFormatted = transactionDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        List<LoanableItem> failedItems = new LinkedList<>();

        // Check for if the loanables are available


        // Create loan transaction
        List<LoanTransaction> transactions = new LinkedList<>();
        loanables.forEach(loanableItem -> transactions.add(new LoanTransaction(loanableItem, client, transactionDate)));

        // Check + create the loan transaction
        loanTransactionGateway.saveAll(transactions.toArray(new LoanTransaction[]{}));

        // Retrieve transaction by userId + transactionDate
        List<?> allTransactions = loanTransactionGateway.findByUserIdAndTransactionDate(client.getId(), transactionDateFormatted);

        // Check between loanables vs. all successful transactions
        for(LoanTransaction t: transactions){
            if(!allTransactions.contains(t)){
                failedItems.add(t.getLoanableItem());
            }
        }

        if (failedItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(failedItems, HttpStatus.EXPECTATION_FAILED);

        }
    }

    public ResponseEntity<?> addReturnTransactions(Client client, List<LoanableItem> loanables){
        LocalDateTime transactionDate = LocalDateTime.now();
        List<ReturnTransaction> transactions = new LinkedList<>();
        loanables.forEach(loanableItem -> transactions.add(new ReturnTransaction(loanableItem, client, transactionDate)));
        returnTransactionGateway.saveAll(transactions.toArray(new ReturnTransaction[]{}));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<?> searchLoanTransactions() {
        return ImmutableList.of(LOAN_TRANSACTION, loanTransactionGateway.findAll());
    }

    public List<?> searchReturnTransactions() {
        return ImmutableList.of(RETURN_TRANSACTION, returnTransactionGateway.findAll());
    }

    public Map<String, List<?>> searchAllByTransactionDate(ObjectNode transactionDate) {
        return ImmutableMap.of(LOAN_TRANSACTION,
                loanTransactionGateway.findByTransactionDate(formatDateString(transactionDate.get(TRANSACTIONDATE).asText())),
                RETURN_TRANSACTION,
                returnTransactionGateway.findByTransactionDate(formatDateString(transactionDate.get(TRANSACTIONDATE).asText())));
    }

    public List<?> searchLoanByTransactionDate(ObjectNode transactionDate) {
        return ImmutableList
                .of(loanTransactionGateway.findByTransactionDate(formatDateString(transactionDate.get(TRANSACTIONDATE).asText())));
    }

    public List<?> searchLoanByDueDate(ObjectNode dueDate) {
        return ImmutableList.of(LOAN_TRANSACTION, loanTransactionGateway.findByDueDate(formatDateString(dueDate.get(DUEDATE).asText())));
    }

    public List<?> searchReturnByTransactionDate(ObjectNode transactionDate) {
        return ImmutableList
                .of(returnTransactionGateway.findByTransactionDate(formatDateString(transactionDate.get(TRANSACTIONDATE).asText())));
    }

    public Map<String, List<?>> searchTransactionsByUserId(Long userId) {
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findByUserId(userId), RETURN_TRANSACTION,
                returnTransactionGateway.findByUserId(userId));
    }

    public List<?> searchLoanByUserId(Long userId) {
        return ImmutableList.of(loanTransactionGateway.findByUserId(userId));
    }

    public List<?> searchReturnByUserId(Long userId) {
        return ImmutableList.of(returnTransactionGateway.findByUserId(userId));
    }

    public Map<String, List<?>> searchTransactionByItemType(String itemType) {
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findByItemType(itemType), RETURN_TRANSACTION,
                returnTransactionGateway.findByItemType(itemType));
    }

    private String formatDateString(String date) {
        return LocalDateTime.parse(date).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
