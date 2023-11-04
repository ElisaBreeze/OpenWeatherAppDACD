package org.ulpgc.dacd.control;

public class OpenWeatherMapProvider implements WeatherProvider {
        private String apikey;

    public OpenWeatherMapProvider(String apikey) {
        this.apikey = apikey;
    }
    public String getApikey() {
        return apikey;
    }
}
