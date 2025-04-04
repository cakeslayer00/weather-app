package com.vladsv.weather_app.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vladsv.weather_app.dto.WeatherCardDto;

import java.io.IOException;

public class WeatherCardDeserializer extends StdDeserializer<WeatherCardDto> {

    public WeatherCardDeserializer() {
        this(null);
    }

    public WeatherCardDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WeatherCardDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        String weather = node.get("weather").findValue("main").asText();
        String location = String.format("%s, %s", node.get("name").asText(), node.get("sys").get("country").asText());
        String temperature = String.valueOf(node.get("main").get("temp").asInt());
        String feelsLike = String.valueOf(node.get("main").get("feels_like").asInt());
        String humidity = node.get("main").get("humidity").asText();
        String icon = node.get("weather").findValue("icon").asText();

        return new WeatherCardDto(weather, location, temperature, feelsLike, humidity, icon);
    }
}
