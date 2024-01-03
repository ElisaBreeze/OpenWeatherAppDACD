package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


//TODO al broker le llegan cientos de eventos e hoteles cuando solo deberian ser 37 => check that, no tiene sentido, el probema estará en el sender
public class EventReceiver {
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private Map<String, JsonArray> weatherEvents = new HashMap<>();
    private Map<String, List<JsonObject>> hotelEvents = new HashMap<>();
    private int counter = 0;
    private final CountDownLatch weatherEventCountDown = new CountDownLatch(50);
    private final CountDownLatch hotelEventCountDown = new CountDownLatch(37);

    public List<JsonObject> messageReceiver() throws StoreException {
        try {
            List<JsonObject> combinedEventsList = new ArrayList<>();
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.setClientID("business-unit");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            createSubscription(session, "prediction.Weather");
            createSubscription(session, "hotel.information");

            weatherEventCountDown.await();
            hotelEventCountDown.await();
            return combineEvents(combinedEventsList);

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
        if(counter == 5) counter = 0;
    }

    private void saveHotelEvent(JsonObject event) {
        String island = event.getAsJsonObject("hotel").get("island").getAsString();

        if (hotelEvents.containsKey(island)) {
            List<JsonObject> eventsList = hotelEvents.get(island);
            eventsList.add(event);
        } else {
            List<JsonObject> eventsList = new ArrayList<>();
            eventsList.add(event);
            hotelEvents.put(island, eventsList);
        }
        hotelEventCountDown.countDown();
    }

    private List<JsonObject> combineEvents(List<JsonObject> combinedEventsList) {
        System.out.println("combineEvents full listas:");
        System.out.println(hotelEvents);
        System.out.println(weatherEvents);
        for (Map.Entry<String, List<JsonObject>> entry : hotelEvents.entrySet()) {
            String location = entry.getKey();
            List<JsonObject> hotelEventList = entry.getValue();

            for (JsonObject hotelEvent : hotelEventList) {
                JsonObject combinedEvent = new JsonObject();
                combinedEvent.addProperty("Location", location);

                JsonArray weatherEventList = weatherEvents.get(location);
                JsonObject combinedWeatherEvents = new JsonObject();

                for (JsonElement weatherEventElement : weatherEventList) {
                    JsonObject weatherEvent = weatherEventElement.getAsJsonObject();
                    combinedWeatherEvents.add(weatherEvent.keySet().iterator().next(), weatherEvent.get(weatherEvent.keySet().iterator().next()));
                }
                combinedEvent.add("CombinedWeatherPredictions", combinedWeatherEvents);

                combinedEvent.add("HotelInformation", hotelEvent);
                combinedEventsList.add(combinedEvent);
            }
        }
        return combinedEventsList;
    }
}
