package com.soen343.project.service.user;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.transaction.Transaction;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.instance.Cart;
import com.soen343.project.service.database.Library;
import com.soen343.project.service.handler.CartHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientHandler{

    private CartHandler cartHandler;
    private Library library;
    private User client;

    @Autowired
    public ClientHandler(CartHandler cartHandler, Library library) {
        this.cartHandler = cartHandler;
        this.library = library;
    }

    public Cart cancelLoan(Long clientId){
        return cartHandler.clear(clientId);
    }

    // TO DO
    public Transaction loanItems(Long clientId){
        setClient(clientId);
        List<LoanableItem> loanables = cartHandler.getLoanables(clientId);
        return null;
    }

    // TO DO
    public Transaction returnItems(Long clientId, List<LoanableItem> loanableItems){
        setClient(clientId);
        return null;
    }

    private void setClient(Long clientId){
        client = library.getUserByID(clientId);
    }
}
