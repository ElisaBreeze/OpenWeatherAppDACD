package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class EventReceiver {
    //TODO change fechas so that they coincide with the 5 day prediction from now
    //TODO change names and format
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private Map<String, JsonArray> weatherEvents = new HashMap<>();
    private Map<String, JsonObject> hotelEvents = new HashMap<>();
    private int counter = 0;

    // CountDownLatch for weather and hotel events
    private final CountDownLatch weatherEventCountDown = new CountDownLatch(40);
    private final CountDownLatch hotelEventCountDown = new CountDownLatch(8);    //TODO si falla es porq antes ponia 7 por alguna razon

    public JsonObject messageReceiver() throws StoreException {
        try {
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.setClientID("business-unit");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            createSubscription(session, "prediction.Weather");
            createSubscription(session, "hotel.information");

            weatherEventCountDown.await();
            hotelEventCountDown.await();
            return combineEvents();

        } catch (JMSException | InterruptedException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }

    }

    private void createSubscription(Session session, String topic) throws StoreException {
        try {
            Topic topicName = session.createTopic(topic);
            MessageConsumer consumer = session.createDurableSubscriber(topicName, "businessUnit-" + topic);
            consumer.setMessageListener(message -> {
                try {
                    messageCreator(message, topic);
                } catch (StoreException exception) {
                    throw new RuntimeException(exception);
                }
            });
        } catch (JMSException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private void messageCreator(Message message, String topic) throws StoreException {
        if (message instanceof TextMessage textMessage) {
            try {
                processEvent(textMessage.getText(), topic);
            } catch (JMSException exception) {
                throw new StoreException(exception.getMessage(), exception);
            }
        }
    }

    private void processEvent(String eventData, String topic) {
        Gson gson = new Gson();
        JsonObject event = gson.fromJson(eventData, JsonObject.class);
        if (topic.equals("prediction.Weather")) {
            saveWeatherEvent(event);
        } else if (topic.equals("hotel.information")) {
            saveHotelEvent(event);
        }
    }

    private void saveWeatherEvent(JsonObject event) {
        String island = event.getAsJsonObject("location").get("island").getAsString();
        counter++;
        JsonObject islandWeatherEvent = new JsonObject();
        islandWeatherEvent.add("prediction" + counter, event);
        weatherEvents.computeIfAbsent(island, k -> new JsonArray()).add(islandWeatherEvent);
        weatherEventCountDown.countDown();
    }

    private void saveHotelEvent(JsonObject event) {
        String island = event.getAsJsonObject("hotel").get("island").getAsString();
        hotelEvents.put(island, event);
        hotelEventCountDown.countDown();
    }

    private JsonObject combineEvents() {
        for (String island : weatherEvents.keySet()) {
            JsonArray event = weatherEvents.get(island);
            JsonObject combinedWeatherEvents = new JsonObject();
            for (JsonElement weatherEvent : event) {
                JsonObject predictionEvent = weatherEvent.getAsJsonObject();
                combinedWeatherEvents.add(predictionEvent.keySet().iterator().next(), predictionEvent.get(predictionEvent.keySet().iterator().next()));
            }

            JsonObject hotelEvent = hotelEvents.getOrDefault(island, new JsonObject());

            JsonObject combinedEvents = new JsonObject();
            combinedEvents.addProperty("Location", island);
            combinedEvents.add("CombinedWeatherEvents", combinedWeatherEvents);
            combinedEvents.add("CombinedHotelEvent", hotelEvent);

            System.out.println("Combined Events: " + combinedEvents);
            return combinedEvents;
        }
        return null; //TODO lo quito?
    }
}

