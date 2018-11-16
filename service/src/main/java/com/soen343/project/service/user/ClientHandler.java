package com.soen343.project.service.user;

import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.user.UserGateway;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.instance.Cart;
import com.soen343.project.service.handler.CartHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientHandler {

    private CartHandler cartHandler;
    private UserGateway userGateway;
    private LoanableItemGateway loanableItemGateway;
    private User client;
    private final long MAX_NUMBER_OF_ITEMS = 3;

    @Autowired
    public ClientHandler(CartHandler cartHandler, UserGateway userGateway, LoanableItemGateway loanableItemGateway) {
        this.cartHandler = cartHandler;
        this.userGateway = userGateway;
        this.loanableItemGateway = loanableItemGateway;
    }

    public Cart cancelLoan(Long clientId){
        return cartHandler.clear(clientId);
    }

    // TO DO
    public Transaction loanItems(Long clientId){
        setClient(clientId);
        List<LoanableItem> loanables = cartHandler.getLoanables(clientId);

        long numberOfOwnedItems = loanableItemGateway.findAll().stream()
                .filter(lItem -> lItem.getClient().getId().equals(clientId))
                .count();
        if(numberOfOwnedItems + loanables.size() >= MAX_NUMBER_OF_ITEMS){

        }



        //Incomplete
        for(int i = 0; i < loanables.size(); i++){
            LoanTransaction loan = new LoanTransaction();
        }

        return null;
    }

    // TO DO
    public Transaction returnItems(Long clientId, List<LoanableItem> loanableItems){
        setClient(clientId);
        return null;
    }

    private void setClient(Long clientId){
        client = userGateway.findById(clientId);
    }
}
