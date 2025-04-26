package com.vladsv.weather_app.service;

import com.vladsv.weather_app.client.OpenWeatherApiClient;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dto.LocationDto;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final LocationDao locationDao;
    private final OpenWeatherApiClient client;

    public List<LocationDto> getLocationsByName(String locationName) {
        return client.getLocationsByNameInJson(locationName);
    }

    public WeatherCardDto mapLocationToWeatherCardDto(Location location) {
        WeatherCardDto weatherCardDto = client.getWeatherByGeoCoordinatesInJson(
                location.getLatitude().toString(),
                location.getLongitude().toString()
        );
        weatherCardDto.setLocation(
                String.format("%s,%s", location.getName(), weatherCardDto.getLocation().split(",")[1]));
        weatherCardDto.setId(location.getId());
        return weatherCardDto;
    }

    public void addLocation(User user, String name, String latitude, String longitude) {
        Location location = getBuiltLocation(name, latitude, longitude, user);

        locationDao.persist(location);
    }

    public void deleteLocation(Long locationId) {
        locationDao.delete(locationId);
    }

    private Location getBuiltLocation(String name, String latitude, String longitude, User user) {
        return Location.builder()
                .name(name)
                .user(user)
                .latitude(BigDecimal.valueOf(Double.parseDouble(latitude)))
                .longitude(BigDecimal.valueOf(Double.parseDouble(longitude)))
                .build();
    }

}
