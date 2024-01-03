package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class InformationDisplay extends Application {
    private static Map<String, JsonObject> bestOptions;

    public void display(Map<String, JsonObject> bestOptions) {
        this.bestOptions = bestOptions;
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Best Options");

        VBox root = new VBox(10); // Vertical spacing between elements

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        for (Map.Entry<String, JsonObject> entry : bestOptions.entrySet()) {
            String key = entry.getKey();
            JsonObject event = entry.getValue();

            Label nameLabel = new Label("Best " + key + " Option:");
            root.getChildren().add(nameLabel);

            Label hotelLabel = new Label("Hotel: " + event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("hotelName").getAsString());
            Label islandLabel = new Label("Island: " + event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("island").getAsString());
            Label locationLabel = new Label("Location: " + event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("city").getAsString());
            root.getChildren().addAll(hotelLabel, islandLabel, locationLabel);

            Label priceLabel = new Label("Total Price: " + event.getAsJsonObject("HotelInformation").get("price").getAsDouble());
            root.getChildren().add(priceLabel);

            printWeatherInformation(root, event.getAsJsonObject("CombinedWeatherPredictions"));

            Label separator = new Label("------------------------------");
            root.getChildren().add(separator);
        }

        primaryStage.setScene(new Scene(scrollPane, 600, 400));
        primaryStage.show();
    }

    private static void printWeatherInformation(VBox root, JsonObject combinedWeatherEvents) {
        Label weatherLabel = new Label("Weather Predictions per Day:");
        root.getChildren().add(weatherLabel);

        for (int i = 1; i <= 5; i++) {
            String predictionNumber = "prediction" + i;
            JsonObject prediction = combinedWeatherEvents.getAsJsonObject(predictionNumber);

            Label dayLabel = new Label("Day " + i + ":");
            Label temperatureLabel = new Label("Temperature: " + prediction.get("temperature").getAsDouble());
            Label humidityLabel = new Label("Humidity: " + prediction.get("humidity").getAsDouble());
            Label precipitationLabel = new Label("Precipitation: " + prediction.get("precipitation").getAsDouble() + "%");
            Label windLabel = new Label("Windspeed: " + prediction.get("wind").getAsDouble());
            Label cloudsLabel = new Label("Clouds: " + prediction.get("clouds").getAsDouble() + "% coverage");

            root.getChildren().addAll(dayLabel, temperatureLabel, humidityLabel, precipitationLabel, windLabel, cloudsLabel);

            Label separator = new Label("------------------------------");
            root.getChildren().add(separator);
        }
    }
}
