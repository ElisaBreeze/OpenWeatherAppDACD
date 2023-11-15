package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.sql.SQLException;
import java.time.Instant;

public interface WeatherStore extends AutoCloseable{
         void save(Weather weather) throws SQLException;

}
