package com.soen343.project.service.transaction;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.media.common.MediaItem;
import com.soen343.project.repository.entity.catalog.itemspec.printed.common.PrintedItem;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.registry.TransactionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRegistry transactionRegistry;

    @Autowired
    public TransactionService(TransactionRegistry transactionRegistry) {
        this.transactionRegistry = transactionRegistry;
    }


    public List<LoanTransaction> createLoanTransactions(User client, List<LoanableItem> loanables) {

        List<LoanTransaction> transactions = new ArrayList<>();

        for (LoanableItem loanableItem : loanables) {
            loanableItem.setClient((Client) client);
            loanableItem.setAvailable(false);

            LocalDateTime rightNow = LocalDateTime.now();
            LocalDateTime dueDate = loanableItem.getSpec() instanceof PrintedItem ? rightNow.plusDays(PrintedItem.MAX_LOAN_DAYS) : rightNow.plusDays(MediaItem.MAX_LOAN_DAYS);

            transactions.add(new LoanTransaction(loanableItem, rightNow, dueDate));
        }

        transactionRegistry.addTransactions(transactions);
        return transactions;
    }
}
