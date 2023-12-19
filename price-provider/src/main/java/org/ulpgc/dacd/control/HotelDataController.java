package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Price;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HotelDataController {

    private final PriceProvider priceProvider;
    private final JMSHotelDataStore hotelDataStore;

    public HotelDataController(PriceProvider priceProvider, JMSHotelDataStore hotelDataStore) {
        this.priceProvider = priceProvider;
        this.hotelDataStore = hotelDataStore;
    }

    public void runTask() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    task();
                } catch (StoreException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }; timer.schedule(timerTask, 0,6*60*60*1000);
    }

    private void task() throws StoreException {
        Map<String, Hotel> hotelsMap = hotelLoader();
        for (Map.Entry<String, Hotel> hotelEntry : hotelsMap.entrySet()) {
            try {
                List<Price> pricePredictions = priceProvider.getPrice(hotelEntry.getValue());
                for (Price price : pricePredictions) {
                    hotelDataStore.save(price);
                }
            } catch (StoreException exception) {
                throw new StoreException(exception.getMessage(), exception);
            }
        }
    }

    public static Map<String, Hotel> hotelLoader() throws StoreException {
        Map<String, Hotel> hotelMap = new HashMap<>();
        try (InputStream inputStream = Objects.requireNonNull(
                HotelDataController.class.getClassLoader().getResourceAsStream("Hotels.csv"));
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] information = line.split("\t");
                Hotel hotel = new Hotel(information[0], information[1], information[2], information[3]); //TODO añadir los datos en ese orden y cambiar a date
                hotelMap.put(information[0], hotel);
            }
        } catch (IOException | NullPointerException | NumberFormatException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
        return hotelMap;
    }

}
