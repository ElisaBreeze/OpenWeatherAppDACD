package org.example;

import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException {
        WeatherEventReceiver weatherEventReceiver = new WeatherEventReceiver();
        weatherEventReceiver.messageReceiver();
    }
}