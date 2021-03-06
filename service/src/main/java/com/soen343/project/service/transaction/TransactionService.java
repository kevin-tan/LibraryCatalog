package com.soen343.project.service.transaction;

import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.registry.TransactionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRegistry transactionRegistry;

    @Autowired
    public TransactionService(TransactionRegistry transactionRegistry, LoanTransactionGateway loanTransactionGateway) {
        this.transactionRegistry = transactionRegistry;
    }

    public ResponseEntity<?> createLoanTransactions(Client client, List<LoanableItem> loanables) {
        return transactionRegistry.addLoanTransactions(client, loanables);
    }

    public ResponseEntity<?> createReturnTransactions(Client client, List<LoanableItem> loanableItems) {
        return transactionRegistry.addReturnTransactions(client, loanableItems);
    }
}
