package com.soen343.project.service.user;

import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.dao.transaction.ReturnTransactionGateway;
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
import java.util.stream.Collectors;

import static org.valid4j.Assertive.*;
import static org.hamcrest.Matchers.*;

@Service
public class ClientHandler {

    private CartHandler cartHandler;
    private UserGateway userGateway;
    private LoanableItemGateway loanableItemGateway;
    private LoanTransactionGateway loanTransactionGateway;
    private ReturnTransactionGateway returnTransactionGateway;
    private TransactionService transactionService;
    private Client client;

    private final long MAX_NUMBER_OF_ITEMS_CLIENT_CAN_LOAN = 3;



    @Autowired
    public ClientHandler(CartHandler cartHandler, UserGateway userGateway, LoanableItemGateway loanableItemGateway,
                         TransactionService transactionService, LoanTransactionGateway loanTransactionGateway, ReturnTransactionGateway returnTransactionGateway) {
        this.cartHandler = cartHandler;
        this.userGateway = userGateway;
        this.loanableItemGateway = loanableItemGateway;
        this.transactionService = transactionService;
        this.loanTransactionGateway = loanTransactionGateway;
        this.returnTransactionGateway = returnTransactionGateway;
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
        int itemsBeingLoanedSize = loanables.size();

        long numberOfOwnedItems = getLoanedItemsByUserID(clientId).size();
        try {
            require(numberOfOwnedItems + loanables.size() <= MAX_NUMBER_OF_ITEMS_CLIENT_CAN_LOAN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        int loanTransactionsPreSize = this.loanTransactionGateway.findByUserId(clientId).size();

        ResponseEntity<?> resp = transactionService.createLoanTransactions(client, loanables);
        cartHandler.clear(clientId);

        ensure(this.loanTransactionGateway.findByUserId(clientId), hasSize(loanTransactionsPreSize + itemsBeingLoanedSize));
        ensure(this.getLoanedItemsByUserID(clientId).stream()
                .allMatch(l -> l.getClient().getId().equals(clientId) && !l.getAvailable()));
        ensure(this.cartHandler.getCartById(clientId).isEmpty());
        return resp;
    }

    public ResponseEntity<?> returnItems(Long clientId, List<LoanableItem> loanableItems) {
        List<LoanableItem> cartItems = this.cartHandler.getLoanables(clientId);
        require(loanableItems.stream().filter(loanableItem ->
                cartItems.stream().anyMatch(l -> l.getId().equals(loanableItem.getId())))
                .collect(Collectors.toList()).isEmpty());

        int returnTransactionsPreSize = this.returnTransactionGateway.findByUserId(clientId).size();
        int loanableItemsPreSize = this.getLoanedItemsByUserID(clientId).size();


        setClient(clientId);
        ResponseEntity<?> resp =  transactionService.createReturnTransactions(client, loanableItems);

        ensure(this.returnTransactionGateway.findByUserId(clientId), hasSize(returnTransactionsPreSize + loanableItems.size()));
        List<LoanableItem> loanableItemsPost = this.getLoanedItemsByUserID(clientId);
        ensure(loanableItemsPost, hasSize(loanableItemsPreSize - loanableItems.size()));
        ensure(loanableItems.stream().filter(loanableItem ->
                loanableItemsPost.stream().anyMatch(l -> l.getId().equals(loanableItem.getId())))
                .collect(Collectors.toList()).isEmpty());
        return resp;
    }

    public List<LoanableItem> getLoanedItemsByUserID(Long clientId) {
        return loanableItemGateway.findByUserIdAndIsLoaned(clientId);
    }

    private void setClient(Long clientId) {
        client = (Client) userGateway.findById(clientId);
    }
}
