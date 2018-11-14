package com.soen343.project.repository.entity.transaction;

import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.user.Client;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ReturnTransaction extends Transaction {

    @Builder
    public ReturnTransaction(Long id, Client client, Item item, String transactionType, Date checkoutDate) {
        super(id, client, item, transactionType, checkoutDate);

    }
}
