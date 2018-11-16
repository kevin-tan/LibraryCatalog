package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soen343.project.service.registry.TransactionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TransactionController {
    private final TransactionRegistry transactionRegistry;

    @Autowired
    public TransactionController(TransactionRegistry transactionRegistry) {
        this.transactionRegistry = transactionRegistry;
    }

    @GetMapping("/transactionHistory/searchAll")
    public ResponseEntity<?> getAllTransactions() {
        return new ResponseEntity<>(transactionRegistry.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/transactionHistory/searchAllLoanTransactions")
    public ResponseEntity<?> getAllLoanTransactions() {
        return new ResponseEntity<>(transactionRegistry.searchLoanTransactions(), HttpStatus.OK);
    }

    @GetMapping("/transactionHistory/searchAllReturnTransaction")
    public ResponseEntity<?> getAllReturnTransactions() {
        return new ResponseEntity<>(transactionRegistry.searchReturnTransactions(), HttpStatus.OK);
    }

    @GetMapping("/transactionHistory/searchByDueDate/{dueDate}")
    public ResponseEntity<?> searchTransactionByDueDate(@PathVariable Date dueDate) {
        return new ResponseEntity<>(transactionRegistry.searchLoanByDueDate(dueDate), HttpStatus.OK);
    }

    @GetMapping("/transactionHistory/search/{itemType}")
    public ResponseEntity<?> searchTransactionByAttribute(@PathVariable String itemType, @RequestBody ObjectNode objectNode) {
        Map<String, String> attributeValue = new HashMap<>();
        objectNode.fieldNames().forEachRemaining(key ->
                attributeValue.put(key,objectNode.get(key).asText())
        );

        return new ResponseEntity<>(transactionRegistry.searchTransactionByAttribute(itemType, attributeValue), HttpStatus.OK);
    }

    @GetMapping("/transactionHistory/searchByUserId/{userId}")
    public ResponseEntity<?> searchTransactionsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionRegistry.searchTransactionsByUserId(userId), HttpStatus.OK);
    }

}
