package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

public interface WeatherProvider{
    public Weather getWeather(Location location);
}
