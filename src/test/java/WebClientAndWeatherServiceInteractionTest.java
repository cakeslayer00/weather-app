import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsv.weather_app.client.OpenWeatherApiClient;
import com.vladsv.weather_app.dto.LocationResponseDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.service.WeatherService;
import config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig(TestConfig.class)
@ActiveProfiles("test")
public class WebClientAndWeatherServiceInteractionTest {

    private AutoCloseable closeable;

    @Mock
    private OpenWeatherApiClient openWeatherApiClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenWeatherService_whenGetLocationsByNameInvoked_thenSuccess() {
        String locationName = "San Francisco";

        String jsonResponse = "[{\"name\": \"San Francisco\",\"lat\": 5.26902235,\"lon\": -72.77388372073908,\"country\": \"CO\",\"state\": \"Casanare\"}]";

        List<LocationResponseDto> locations = List.of(
                new LocationResponseDto("San Francisco",
                        new BigDecimal("5.26902235"),
                        new BigDecimal("-72.77388372073908"),
                        "CO",
                        "Casanare"));

        try {
            when(openWeatherApiClient.getLocationsByNameInJson(locationName)).thenReturn(locations);
            when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(locations);

            assertEquals(locations.getFirst(), weatherService.getLocationsByName(locationName).getFirst());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void givenWebClient_whenInvalidGetWeatherRequestSent_thenExceptionThrown() {
        Location location = new Location(0L, "San Francisco", new User(), new BigDecimal("180"), new BigDecimal("180"));

        when(openWeatherApiClient.getWeatherByGeoCoordinatesInJson(any(), any())).thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class, () -> weatherService.mapLocationToWeatherCardDto(location));
    }

    @AfterEach
    public void closeMocks() {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
