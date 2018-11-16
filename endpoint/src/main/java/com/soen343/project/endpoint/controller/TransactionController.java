package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soen343.project.service.registry.TransactionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/transactionHistory")
public class TransactionController {
    private final TransactionRegistry transactionRegistry;

    @Autowired
    public TransactionController(TransactionRegistry transactionRegistry) {
        this.transactionRegistry = transactionRegistry;
    }

    @GetMapping("/searchAll")
    public ResponseEntity<?> getAllTransactions() {
        return new ResponseEntity<>(transactionRegistry.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/searchAllLoanTransactions")
    public ResponseEntity<?> getAllLoanTransactions() {
        return new ResponseEntity<>(transactionRegistry.searchLoanTransactions(), HttpStatus.OK);
    }

    @GetMapping("/searchAllReturnTransaction")
    public ResponseEntity<?> getAllReturnTransactions() {
        return new ResponseEntity<>(transactionRegistry.searchReturnTransactions(), HttpStatus.OK);
    }

    @PostMapping("/searchAllByTransactionDate")
    public ResponseEntity<?> searchTransactionByTransactionDate(@RequestBody ObjectNode transactionDate) {
        return new ResponseEntity<>(transactionRegistry.searchAllByTransactionDate(transactionDate), HttpStatus.OK);
    }

    @PostMapping("/loanTransactions/searchByDueDate")
    public ResponseEntity<?> searchTransactionByDueDate(@RequestBody ObjectNode dueDate) {
        return new ResponseEntity<>(transactionRegistry.searchLoanByDueDate(dueDate), HttpStatus.OK);
    }

    @PostMapping("/loanTransactions/searchByTransactionDate")
    public ResponseEntity<?> searchLoanTransactionByTransactionDate(@RequestBody ObjectNode transactionDate) {
        return new ResponseEntity<>(transactionRegistry.searchLoanByTransactionDate(transactionDate), HttpStatus.OK);
    }

    @PostMapping("/returnTransactions/searchByTransactionDate")
    public ResponseEntity<?> searchReturnTransactionByTransactionDate(@RequestBody ObjectNode transactionDate) {
        return new ResponseEntity<>(transactionRegistry.searchReturnByTransactionDate(transactionDate), HttpStatus.OK);
    }

    @GetMapping("/searchByUserId/{userId}")
    public ResponseEntity<?> searchTransactionsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionRegistry.searchTransactionsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/loanTransaction/searchByUserId/{userId}")
    public ResponseEntity<?> searchLoanTransactionsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionRegistry.searchLoanByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/returnTransaction/searchByUserId/{userId}")
    public ResponseEntity<?> searchReturnTransactionsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionRegistry.searchReturnByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/search/{itemType}")
    public ResponseEntity<?> searchTransactionByAttribute(@PathVariable String itemType) {
        return new ResponseEntity<>(transactionRegistry.searchTransactionByItemType(itemType), HttpStatus.OK);
    }

}
