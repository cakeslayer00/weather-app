import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.dto.UserRequestDto;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.sql.EntityPersistenceException;
import com.vladsv.weather_app.service.AuthService;
import config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(TestConfig.class)
@ActiveProfiles("test")
public class AuthorizationServiceAndPersistenceLayerInteractionTest {

    private AuthService authService;
    private UserDao userDao;
    private SessionDao sessionDao;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setup(WebApplicationContext wac, @Autowired MockHttpServletResponse response) {
        this.authService = wac.getBean(AuthService.class);
        this.userDao = wac.getBean(UserDao.class);
        this.sessionDao = wac.getBean(SessionDao.class);
        this.response = response;
    }

    @Test
    public void givenAuthService_whenRegistrationMethodInvoked_thenNewUserAdded() {
        UserRequestDto dto = new UserRequestDto("testusername", "1234asdf", "1234asdf");

        assertDoesNotThrow(() -> authService.register(dto, response));
        User user = userDao.findByUsername(dto.getUsername()).get();
        Session session = sessionDao.findSessionByUser(user).get();
        assertNotNull(user);
        assertNotNull(session);
    }

    @Test
    @AfterTestMethod
    public void givenExistingUser_whenRegisteringNewUserWithSameName_thenExceptionThrown() {
        UserRequestDto dto = new UserRequestDto("testusername", "1234asdf", "1234asdf");

        assertThrows(EntityPersistenceException.class,
                () -> authService.register(dto, response));
    }

    @Test
    @AfterTestMethod
    public void givenExistingUser_whenAuthorizationIfSessionExpired_thenSessionExpiryTimeUpdated() {
        UserRequestDto dto = new UserRequestDto("testusername", "1234asdf", "1234asdf");

        User user = userDao.findByUsername(dto.getUsername()).get();
        Session before = sessionDao.findSessionByUser(user).get();
        before.setExpiryAt(LocalDateTime.now().minusMinutes(1));
        sessionDao.update(before);

        authService.authorize(dto, response);
        Session after = sessionDao.findSessionByUser(user).get();

        assertTrue(after.getExpiryAt().isAfter(before.getExpiryAt()));

    }

}
