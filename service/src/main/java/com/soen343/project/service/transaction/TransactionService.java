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
    private final LoanTransactionGateway loanTransactionGateway;

    @Autowired
    public TransactionService(TransactionRegistry transactionRegistry, LoanTransactionGateway loanTransactionGateway) {
        this.transactionRegistry = transactionRegistry;
        this.loanTransactionGateway = loanTransactionGateway;
    }

    public ResponseEntity<?> createLoanTransactions(Client client, List<LoanableItem> loanables) {
        return transactionRegistry.addTransactions(client, loanables);
    }

    // todo: implement
    public void createReturnTransactions(User client, List<LoanableItem> loanableItems) {

    }
}
