package com.soen343.project.service.handler;

import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.instance.Cart;
import com.soen343.project.service.authenticate.LoginManager;
import com.soen343.project.service.notification.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartHandler implements Observer<User> {

    private Map<Long, Cart> carts;

    @Autowired
    public CartHandler(LoginManager loginManager) {
        loginManager.addObserver(this);
        this.carts = new HashMap<>();
    }

    public Cart getCartById(Long clientId) {
        return carts.get(clientId);
    }

    @Override
    public void update(User user, boolean userIsLoggingIn) {
        if (!(user instanceof Client)) {
            return;
        }

        if (userIsLoggingIn) {
            carts.put(user.getId(), new Cart());
        } else {
            carts.remove(user.getId());
        }
    }

    public Cart addItemToCart(Long clientId, LoanableItem loanableItem) {
        Cart cart = carts.get(clientId);
        cart.addItemToCart(loanableItem);
        return cart;
    }

    public Cart removeItemFromCart(Long clientId, LoanableItem loanableItem) {
        Cart cart = carts.get(clientId);
        cart.removeItemFromCart(loanableItem);
        return cart;
    }

    public Cart clear(Long clientId) {
        Cart cart = carts.get(clientId);
        cart.clear();
        return cart;
    }
}
