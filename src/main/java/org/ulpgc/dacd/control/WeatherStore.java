package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.time.Instant;

public interface WeatherStore extends AutoCloseable{
         void save(Weather weather);

}
