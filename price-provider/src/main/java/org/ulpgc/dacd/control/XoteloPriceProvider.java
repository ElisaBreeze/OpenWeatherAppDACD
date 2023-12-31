package org.ulpgc.dacd.control;

import com.google.gson.*;
import org.ulpgc.dacd.model.Price;
import org.ulpgc.dacd.model.Hotel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XoteloPriceProvider implements PriceProvider {
    @Override
    public List<Price> getPrice(Hotel hotel) throws StoreException {
        List<Price> priceList = new ArrayList<>();
        String apiCall = apiCall(hotel);
        try {
            HttpURLConnection httpURLConnection = connection(apiCall);
            if (connection(apiCall).getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonObject jsonObject = new Gson().fromJson(responseReader(httpURLConnection), JsonObject.class);
                arrayProcessing(jsonObject, priceList, hotel);
            }
            return priceList;
        } catch (IOException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }
    private String apiCall(Hotel hotel) {
        return "https://data.xotelo.com/api/rates" +
                "?hotel_key=" + hotel.getHotelKey() + "&currency=EUR" +
                "&chk_in=" + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                "&chk_out=" + LocalDate.now().plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private HttpURLConnection connection(String apiCall) throws StoreException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(apiCall).openConnection();
            httpURLConnection.setRequestMethod("GET");
            return httpURLConnection;
        } catch (IOException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private String responseReader(HttpURLConnection httpURLConnection) throws StoreException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);

            }
            return response.toString();
        } catch (IOException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private void arrayProcessing(JsonObject jsonObject, List<Price> priceList, Hotel hotel) {
        JsonArray rates = jsonObject.getAsJsonObject("result").get("rates").getAsJsonArray();
        if (!rates.isEmpty()) {
            priceList.add(new Price(hotel, rates.get(0).getAsJsonObject().get("rate").getAsDouble(), Instant.now(), "price-provider.Xotelo"));
        } else {
            Price price = new Price(hotel, 0.0, Instant.now(), "price-provider.Xotelo");
            priceList.add(price);
        }
    }
}
