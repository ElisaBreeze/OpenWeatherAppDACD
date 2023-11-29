package org.ulpgc.dacd;

import org.ulpgc.dacd.control.StoreExceptions;

public class Main {
    public static void main(String[] args) throws StoreExceptions {
        WeatherEventReceiver weatherEventReceiver = new WeatherEventReceiver();
        weatherEventReceiver.messageReceiver();
    }
}