package com.soen343.project.service.registry;

import com.google.common.collect.ImmutableMap;
import com.soen343.project.repository.dao.Gateway;
import com.soen343.project.repository.dao.mapper.GatewayMapper;
import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.dao.transaction.ReturnTransactionGateway;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TransactionRegistry {
    private final GatewayMapper gatewayMapper;
    private final LoanTransactionGateway loanTransactionGateway;
    private final ReturnTransactionGateway returnTransactionGateway;

    private static final String LOAN_TRANSACTION = "loanTransaction";
    private static final String RETURN_TRANSACTION = "returnTransaction";

    public TransactionRegistry(GatewayMapper gatewayMapper, LoanTransactionGateway loanTransactionGateway, ReturnTransactionGateway returnTransactionGateway){
        this.gatewayMapper = gatewayMapper;
        this.loanTransactionGateway = loanTransactionGateway;
        this.returnTransactionGateway = returnTransactionGateway;
    }

    public Map<String, List<?>> getAllTransactions(){
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findAll(), RETURN_TRANSACTION, returnTransactionGateway.findAll());
    }

    public Map<String, List<?>> searchLoanTransactions(){
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findAll());
    }

    public Map<String, List<?>> searchReturnTransactions(){
        return ImmutableMap.of(RETURN_TRANSACTION, returnTransactionGateway.findAll());
    }

    public Map<String, List<?>> searchLoanByDueDate(Date dueDate){
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findByDueDate(dueDate));
    }

    // TODO: Fix implementation
    public List<?> searchTransactionByAttribute(String itemType, Map<String, String> attributeValue) {
        Gateway gateway = gatewayMapper.getGateway(itemType);

        return gateway.findByAttribute(attributeValue);
    }

    public Map<String, List<?>> searchTransactionsByUserId(Long userId){
        return ImmutableMap.of(LOAN_TRANSACTION, loanTransactionGateway.findByUserId(userId), RETURN_TRANSACTION, returnTransactionGateway.findByUserId(userId));
    }


}
