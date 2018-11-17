package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.service.handler.CartHandler;
import com.soen343.project.service.user.ClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/client")
public class LoanController {

    private final CartHandler cartHandler;
    private final ClientHandler clientHandler;

    @Autowired
    public LoanController(CartHandler cartHandler, ClientHandler clientHandler) {
        this.cartHandler = cartHandler;
        this.clientHandler = clientHandler;
    }

    @PostMapping("/cart/{clientId}/add")
    public ResponseEntity<?> addItemToCart(@PathVariable Long clientId, @RequestBody LoanableItem loanableItem) {
        return new ResponseEntity<>(cartHandler.addItemToCart(clientId, loanableItem), HttpStatus.OK);
    }

    @PostMapping("/cart/{clientId}/remove")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long clientId, @RequestBody LoanableItem loanableItem) {
        return new ResponseEntity<>(cartHandler.removeItemFromCart(clientId, loanableItem), HttpStatus.OK);
    }

    @PostMapping("/{clientId}/cancelLoan")
    public ResponseEntity<?> cancelLoan(@PathVariable Long clientId) {
        return new ResponseEntity<>(clientHandler.cancelLoan(clientId), HttpStatus.OK);
    }

    @PostMapping("/{clientId}/loan")
    public ResponseEntity<?> loanItems(@PathVariable Long clientId) {
        return clientHandler.loanItems(clientId);
    }

    @PostMapping("/{clientId}/return")
    public ResponseEntity<?> returnItems(@PathVariable Long clientId, @RequestBody List<LoanableItem> loanableItems) {
        return new ResponseEntity<>(clientHandler.returnItems(clientId, loanableItems), HttpStatus.OK);
    }

    @GetMapping("/{clientId}/loanedItem")
    public ResponseEntity<?> searchTransactionByAttribute(@PathVariable Long clientId) {
        return new ResponseEntity<>(clientHandler.getLoanedItemsByUserID(clientId), HttpStatus.OK);
    }

}
