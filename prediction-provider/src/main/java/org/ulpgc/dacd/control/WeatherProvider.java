package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.util.List;

public interface WeatherProvider{
    List<Weather> getWeather(Location location) throws StoreExceptions;
}
