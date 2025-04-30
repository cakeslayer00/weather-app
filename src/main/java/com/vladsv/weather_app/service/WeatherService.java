package com.vladsv.weather_app.service;

import com.vladsv.weather_app.client.OpenWeatherApiClient;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dto.LocationRequestDto;
import com.vladsv.weather_app.dto.LocationResponseDto;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final LocationDao locationDao;
    private final OpenWeatherApiClient client;
    private final ModelMapper modelMapper;

    public List<LocationResponseDto> getLocationsByName(String locationName) {
        return client.getLocationsByNameInJson(locationName);
    }

    public WeatherCardDto mapLocationToWeatherCardDto(Location location) {
        WeatherCardDto weatherCardDto = client.getWeatherByGeoCoordinatesInJson(
                location.getLatitude().toString(),
                location.getLongitude().toString()
        );
        weatherCardDto.setId(location.getId());
        weatherCardDto.setLocation(location.getName());
        return weatherCardDto;
    }

    public void addLocation(User user, LocationRequestDto locationRequestDto) {
        Location location = modelMapper.map(locationRequestDto, Location.class);
        location.setUser(user);

        locationDao.persist(location);
    }

    public void deleteLocation(Long locationId) {
        locationDao.delete(locationId);
    }

}
