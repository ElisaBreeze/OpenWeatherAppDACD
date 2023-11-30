package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

public interface WeatherStore extends AutoCloseable{
    void save(Weather weather) throws StoreExceptions;
}
