package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.service.handler.CartHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class LoanController {

    private final CartHandler cartHandler;

    @Autowired
    public LoanController(CartHandler cartHandler) {
        this.cartHandler = cartHandler;
    }

    @PostMapping("/cart/{clientId}/add")
    public ResponseEntity<?> addItemToCart(@PathVariable Long clientId, @RequestBody LoanableItem loanableItem) {
        return new ResponseEntity<>(cartHandler.addItemToCart(clientId, loanableItem), HttpStatus.OK);
    }

    @PostMapping("/cart/{clientId}/remove")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long clientId, @RequestBody LoanableItem loanableItem) {
        return new ResponseEntity<>(cartHandler.removeItemFromCart(clientId, loanableItem), HttpStatus.OK);
    }
}
