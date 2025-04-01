import com.vladsv.weather_app.config.PersistenceConfig;
import com.vladsv.weather_app.config.WebApplicationConfig;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.dto.UserDto;
import com.vladsv.weather_app.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitWebConfig(classes = {WebApplicationConfig.class, PersistenceConfig.class})
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
        UserDto user = new UserDto("testusername", "1234asdf");

        assertDoesNotThrow(() -> authService.register(user, response));
        assertNotNull(userDao.findByUsername(user.getUsername()));
    }

}
