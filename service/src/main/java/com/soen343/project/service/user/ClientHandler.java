package com.soen343.project.service.user;

import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.user.UserGateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.instance.Cart;
import com.soen343.project.service.handler.CartHandler;
import com.soen343.project.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientHandler {

    private CartHandler cartHandler;
    private UserGateway userGateway;
    private LoanableItemGateway loanableItemGateway;
    private TransactionService transactionService;
    private Client client;

    private final long MAX_NUMBER_OF_ITEMS_CLIENT_CAN_LOAN = 3;

    @Autowired
    public ClientHandler(CartHandler cartHandler, UserGateway userGateway, LoanableItemGateway loanableItemGateway, TransactionService transactionService) {
        this.cartHandler = cartHandler;
        this.userGateway = userGateway;
        this.loanableItemGateway = loanableItemGateway;
        this.transactionService = transactionService;
    }

    public Cart cancelLoan(Long clientId) {
        return cartHandler.clear(clientId);
    }

    public ResponseEntity<?> loanItems(Long clientId) {
        setClient(clientId);
        List<LoanableItem> loanables = cartHandler.getLoanables(clientId);
        cartHandler.clear(clientId);

        long numberOfOwnedItems = loanableItemGateway.findAll().stream()
                .filter(lItem -> lItem.getClient().getId().equals(clientId))
                .count();

        if (numberOfOwnedItems + loanables.size() >= MAX_NUMBER_OF_ITEMS_CLIENT_CAN_LOAN) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return transactionService.createLoanTransactions(client,loanables);
    }

    // TODO: Done
    public Transaction returnItems(Long clientId, List<LoanableItem> loanableItems) {
        setClient(clientId);
        transactionService.createReturnTransactions(client,loanableItems);
        return null;
    }

    private void setClient(Long clientId) {
        client = (Client) userGateway.findById(clientId);
    }
}