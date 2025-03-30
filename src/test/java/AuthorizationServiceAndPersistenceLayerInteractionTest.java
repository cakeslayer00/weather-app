import com.vladsv.weather_app.config.PersistenceConfig;
import com.vladsv.weather_app.config.WebApplicationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

@SpringJUnitWebConfig(classes = {WebApplicationConfig.class, PersistenceConfig.class})
@ActiveProfiles("test")
public class AuthorizationServiceAndPersistenceLayerInteractionTest {

    @Test
    public void givenAuthService_whenRegistrationMethodInvoked_thenNewUserAdded() {

    }

}
