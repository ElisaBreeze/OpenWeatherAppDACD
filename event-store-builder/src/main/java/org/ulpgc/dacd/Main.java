package org.ulpgc.dacd;

public class Main {
    public static void main(String[] args) throws StoreExceptions {
        WeatherEventReceiver weatherEventReceiver = new WeatherEventReceiver(args[0]);
        weatherEventReceiver.messageReceiver();
    }
}
