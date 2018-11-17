package com.soen343.project.service.transaction;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.registry.TransactionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRegistry transactionRegistry;

    @Autowired
    public TransactionService(TransactionRegistry transactionRegistry) {
        this.transactionRegistry = transactionRegistry;
    }


    public void createLoanTransactions(User client, List<LoanableItem> loanables) {

        // TODO: remove this. Just showing another way you can look at it.
//        List<LoanTransaction> transactions = loanables.stream()
//                .map(loanableItem -> new LoanTransaction(loanableItem, (Client) client))
//                .collect(Collectors.toList());

        List<LoanTransaction> transactions = new ArrayList<>();
        for (LoanableItem loanableItem : loanables) {
            transactions.add(new LoanTransaction(loanableItem, (Client)client));
        }

        transactionRegistry.addTransactions(transactions); // TODO: returns items that are already loaned if fails
    }

    // todo: implement
    public void createReturnTransactions(User client, List<LoanableItem> loanableItems) {

    }
}
