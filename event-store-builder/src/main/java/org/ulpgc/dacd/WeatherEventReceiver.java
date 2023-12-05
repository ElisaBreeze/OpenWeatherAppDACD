package org.ulpgc.dacd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WeatherEventReceiver {
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final String topic = "prediction.Weather";
    private final String eventStoringPath = "eventStore";
    private final String ss = "prediction-provider";

    public static void main(String[] args) {
        try {
            new WeatherEventReceiver().messageReceiver();
        } catch (StoreExceptions exception) {
            throw new RuntimeException(exception);
        }
    }

    public void messageReceiver() throws StoreExceptions {
        try {
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.setClientID("event-store-builder");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topicDestination = session.createTopic(topic);
            MessageConsumer consumer = session.createDurableSubscriber(topicDestination,"eventStoreBuilder-prediction.Weather");
            consumer.setMessageListener(message -> {
                try {
                    messageCreator(message);
                } catch (StoreExceptions exceptions) {
                    throw new RuntimeException(exceptions);
                }
            });
        } catch (JMSException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
    }

    private void messageCreator(Message message) throws StoreExceptions {
        if (message instanceof TextMessage textMessage) {
            try {
                saveEvent(textMessage.getText());
                System.out.println("Datos: " + textMessage.getText()); //TODO quitarlo
            } catch (JMSException exception) {
                throw new StoreExceptions(exception.getMessage(), exception);
            }
        }
    }

    private void saveEvent(String event) throws StoreExceptions {
        File file = fileOf(event);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
                fileWriter.write(event + "\n");
        } catch (IOException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
    }

    private File fileOf(String event) throws StoreExceptions {
        JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
        String ts = jsonObject.get("timeStamp").getAsString();
        String ss = jsonObject.get("ss").getAsString();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String timeStamp = LocalDate.parse(ts).format(dateTimeFormatter);
        Path filePath =  Paths.get(eventStoringPath, "prediction.Weather", ss, timeStamp + ".events");
        directoryCreator(filePath.getParent());
        return filePath.toFile();
    }

    private void directoryCreator(Path directoryPath) throws StoreExceptions {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException exception) {
                throw new StoreExceptions(exception.getMessage(), exception);
            }
        }
    }
}
