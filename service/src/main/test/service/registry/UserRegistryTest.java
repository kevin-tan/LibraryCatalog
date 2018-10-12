package service.registry;

import com.soen343.project.service.authenticate.AuthenticationService;
import com.soen343.project.service.database.RecordDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistryTest {

    private RecordDatabase recordDatabase;
    private AuthenticationService authenticationService;

    @Before
    public void setup() {
        recordDatabase = Mockito.mock(RecordDatabase.class);
        authenticationService = Mockito.mock(AuthenticationService.class);
    }
}
