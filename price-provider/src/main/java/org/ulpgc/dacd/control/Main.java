package org.ulpgc.dacd.control;

public class Main {
    public static void main(String[] args) {
        XoteloPriceProvider priceProvider = new XoteloPriceProvider();
        JMSHotelDataStore priceStore = new JMSHotelDataStore();
        HotelDataController hotelDataControllerController = new HotelDataController(priceProvider, priceStore);
        hotelDataControllerController.runTask();
    }
}

//TODO en readme poner que los datos del precio tardan algo más en salir y que se espera.