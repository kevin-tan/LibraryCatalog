package com.soen343.project.repository.dao.transaction.com;

import com.soen343.project.repository.dao.Gateway;

import java.util.List;

/**
 * Created by Kevin Tan 2018-11-16
 */

public interface TransactionGateway<E> extends Gateway<E> {

    List<?> findByUserId(Long userId);

    List<?> findByTransactionDate(String transactionDate);

    List<?> findByItemType(String itemType);
}
