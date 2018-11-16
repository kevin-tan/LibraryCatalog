package com.soen343.project.repository.instance;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<LoanableItem> loanableItems;

    public Cart() {
        this.loanableItems = new ArrayList<>();
    }

    public void addItemToCart(LoanableItem loanableItem) {
        this.loanableItems.add(loanableItem);
    }

    public void removeItemFromCart(LoanableItem loanableItem) {
        this.loanableItems.remove(loanableItem);
    }

}