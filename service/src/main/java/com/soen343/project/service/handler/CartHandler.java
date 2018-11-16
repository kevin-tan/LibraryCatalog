package com.soen343.project.service.handler;

import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.instance.Cart;
import com.soen343.project.service.notification.Observer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartHandler implements Observer<User> {

    private Map<Long, Cart> carts;

    public CartHandler() {
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
}
