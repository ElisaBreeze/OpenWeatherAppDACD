package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import javax.jms.JMSException;

public interface WeatherStore extends AutoCloseable{
    void save(Weather weather) throws JMSException;
}
