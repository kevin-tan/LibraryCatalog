package com.soen343.project.service.handler;

import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.authenticate.LoginManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class CartHandlerTest {

    private CartHandler cartHandler;
    private LoginManager loginManager;

    @Before
    public void setup() {
        loginManager = Mockito.mock(LoginManager.class);
        this.cartHandler = new CartHandler(loginManager);
    }

    @Test
    public void ensureCartIsAddedForClients() {
        User client = new Client(1L, "", "", "", "", "", "");
        this.cartHandler.update(client, true);
        assertNotNull(cartHandler.getCartById(1L));
    }

    @Test
    public void ensureNoCartIsAddedForAdmins() {
        User admin = new Admin(1L, "", "", "", "", "", "");
        this.cartHandler.update(admin, true);
        assertNull(cartHandler.getCartById(1L));
    }
}
