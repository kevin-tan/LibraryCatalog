package com.soen343.project.service.user;

import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.dao.user.UserGateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.instance.Cart;
import com.soen343.project.service.handler.CartHandler;
import com.soen343.project.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.valid4j.Assertive.*;
import static org.hamcrest.Matchers.*;

@Service
public class ClientHandler {

    private CartHandler cartHandler;
    private UserGateway userGateway;
    private LoanableItemGateway loanableItemGateway;
    private LoanTransactionGateway loanTransactionGateway;
    private TransactionService transactionService;
    private Client client;

    private final long MAX_NUMBER_OF_ITEMS_CLIENT_CAN_LOAN = 3;



    @Autowired
    public ClientHandler(CartHandler cartHandler, UserGateway userGateway, LoanableItemGateway loanableItemGateway,
                         TransactionService transactionService) {
        this.cartHandler = cartHandler;
        this.userGateway = userGateway;
        this.loanableItemGateway = loanableItemGateway;
        this.transactionService = transactionService;
    }

    public Cart cancelLoan(Long clientId) {
        require(!this.cartHandler.getCartById(clientId).isEmpty());

        Cart cart = cartHandler.clear(clientId);

        ensure(cart.isEmpty());
        return cart;
    }

    public ResponseEntity<?> loanItems(Long clientId) {
        try {
            require(!this.cartHandler.getCartById(clientId).isEmpty());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        setClient(clientId);
        List<LoanableItem> loanables = cartHandler.getLoanables(clientId);

        long numberOfOwnedItems = getLoanedItemsByUserID(clientId).size();
        try {
            require(numberOfOwnedItems + loanables.size() <= MAX_NUMBER_OF_ITEMS_CLIENT_CAN_LOAN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        int loanTransactionsPreSize = this.loanTransactionGateway.findByUserId(clientId).size();

        ResponseEntity<?> resp = transactionService.createLoanTransactions(client, loanables);
        cartHandler.clear(clientId);

        ensure(this.loanTransactionGateway.findByUserId(clientId), hasSize(loanTransactionsPreSize + loanables.size()));
        ensure(this.getLoanedItemsByUserID(clientId).stream()
                .allMatch(l -> l.getClient().getId().equals(clientId) && !l.getAvailable()));
        ensure(this.cartHandler.getCartById(clientId).isEmpty());
        return resp;
    }

    public ResponseEntity<?> returnItems(Long clientId, List<LoanableItem> loanableItems) {
        setClient(clientId);
        return transactionService.createReturnTransactions(client, loanableItems);
    }

    public List<LoanableItem> getLoanedItemsByUserID(Long clientId) {
        return loanableItemGateway.findByUserIdAndIsLoaned(clientId);
    }

    private void setClient(Long clientId) {
        client = (Client) userGateway.findById(clientId);
    }
}
