package com.soen343.project.service.registry;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.service.authenticate.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private UserRepository userRepository;

    private static final Admin VALID_ADMIN = new Admin(1L, "test", "test", "test", "test", "test", "test");
    private static final Client VALID_CLIENT = new Client(2L, "test", "test", "test", "test", "test", "test");

    @Before
    public void setup() {
        userRepository =  Mockito.mock(UserRepository.class);
        authenticationService = new AuthenticationService(userRepository);
    }

    @Test
    public void whenAuthenticatingAdmin_thenReturnTrue() {
        Mockito.when(userRepository.findById(VALID_ADMIN.getId())).thenReturn(VALID_ADMIN);
        assertTrue(authenticationService.authenticateAdmin(VALID_ADMIN.getId()));
    }

    @Test
    public void whenAuthenticationClient_thenReturnFalse() {
        Mockito.when(userRepository.findById(VALID_CLIENT.getId())).thenReturn(VALID_CLIENT);
        assertFalse(authenticationService.authenticateAdmin(VALID_CLIENT.getId()));
    }
}
