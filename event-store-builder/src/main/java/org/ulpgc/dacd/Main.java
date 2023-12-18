package org.ulpgc.dacd;

public class Main {
    public static void main(String[] args) throws StoreException {
        WeatherEventReceiver weatherEventReceiver = new WeatherEventReceiver(args[0]);
        weatherEventReceiver.messageReceiver();

        PriceEventReceiver priceEventReceiver = new PriceEventReceiver(args[1]);
        priceEventReceiver.messageReceiver();
    }
}
