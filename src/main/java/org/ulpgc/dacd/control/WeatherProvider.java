package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.util.List;

public interface WeatherProvider{
    public List<Weather> getWeather(Location location);
}
