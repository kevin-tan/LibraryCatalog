package com.soen343.project.repository.instance;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;

import java.util.ArrayList;
import java.util.List;

import static org.valid4j.Assertive.*;
import static org.hamcrest.Matchers.*;

public class Cart {

    private List<LoanableItem> loanableItems;

    public Cart() {
        this.loanableItems = new ArrayList<>();
    }

    public void addItemToCart(LoanableItem loanableItem) {
        require(loanableItem.getAvailable());
        require(this.loanableItems.stream().noneMatch(l -> l.getId().equals(loanableItem.getId())));
        int loanableItemsPreSize = this.loanableItems.size();

        this.loanableItems.add(loanableItem);

        ensure(this.loanableItems.stream().anyMatch(l -> l.getId().equals(loanableItem.getId())));
        ensure(this.loanableItems, hasSize(loanableItemsPreSize + 1));
    }

    public void removeItemFromCart(LoanableItem loanableItem) {
        require(this.loanableItems.stream().anyMatch(l -> l.getId().equals(loanableItem.getId())));
        int loanableItemsPreSize = this.loanableItems.size();

        this.loanableItems.removeIf(cartItem -> cartItem.getId().equals(loanableItem.getId()));

        ensure(this.loanableItems, hasSize(loanableItemsPreSize - 1));
        ensure(this.loanableItems.stream().noneMatch(l -> l.getId().equals(loanableItem.getId())));
    }

    public void clear(){
        loanableItems.clear();
    }

    public boolean isEmpty() {
        return this.loanableItems.isEmpty();
    }

    public List<LoanableItem> getAll(){
        return loanableItems;
    }

}
