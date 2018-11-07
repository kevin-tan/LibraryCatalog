package com.soen343.project.service.registry;

import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.instance.ActiveUser;
import com.soen343.project.service.database.RecordDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistryTest {

    private RecordDatabase recordDatabase;

    private UserRegistry userRegistry;
    private List<ActiveUser> expectedActiveUsers;

    @Before
    public void setup() {
        recordDatabase = Mockito.mock(RecordDatabase.class);
        userRegistry = new UserRegistry(recordDatabase);
        expectedActiveUsers = new LinkedList<>();
        for (long i = 1; i < 6; ++i){
            ActiveUser activeUser = new ActiveUser(i);
            userRegistry.activeUsers.add(activeUser);
            expectedActiveUsers.add(activeUser);
        }
    }

    @Test
    public void testViewActiveUserRegistry_asAdmin() {
        assertThat(userRegistry.viewActiveUserRegistry(), is(expectedActiveUsers));
    }

    @Test
    public void testUpdate_activeUserRemoved() {
        User user = new Client(1L, "", "", "", "", "", "");

        expectedActiveUsers.remove(0);
        userRegistry.update(user);
        assertThat(userRegistry.activeUsers, is(expectedActiveUsers));
    }

    @Test
    public void testUpdate_activeUserAdded() {
        User user = new Client(6L, "", "", "", "", "", "");

        expectedActiveUsers.add(new ActiveUser(6L));
        userRegistry.update(user);
        assertThat(userRegistry.activeUsers, is(expectedActiveUsers));
    }
}
