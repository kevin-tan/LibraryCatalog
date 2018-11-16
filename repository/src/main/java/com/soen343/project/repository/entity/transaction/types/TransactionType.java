package com.soen343.project.repository.entity.transaction.types;

import com.soen343.project.repository.entity.transaction.LoanTransaction;
import com.soen343.project.repository.entity.transaction.ReturnTransaction;


public final class TransactionType {

    private TransactionType() {}

    public static String LOANTRANSACTION = LoanTransaction.class.getSimpleName();
    public static String RETURNTRANSACTION = ReturnTransaction.class.getSimpleName();

}