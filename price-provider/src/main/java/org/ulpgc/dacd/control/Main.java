package org.ulpgc.dacd.control;

public class Main {
    public static void main(String[] args) {
        XoteloPriceProvider priceProvider = new XoteloPriceProvider(args[0], args[1]);
        JMSHotelDataStore priceStore = new JMSHotelDataStore();
        HotelDataController hotelDataControllerController = new HotelDataController(priceProvider, priceStore);
        hotelDataControllerController.runTask();
    }
}
//TODO cambiar nombre prediction price