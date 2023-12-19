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
    private final String eventStoringPath;

    public WeatherEventReceiver(String eventStoringPath){ this.eventStoringPath = eventStoringPath; }

    public void messageReceiver() throws StoreException {
        try {
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.setClientID("event-store-builder");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topicName = session.createTopic("prediction.Weather");
            MessageConsumer consumer = session.createDurableSubscriber(topicName,"eventStoreBuilder-prediction.Weather");
            consumer.setMessageListener(message -> {
                try {
                    messageCreator(message);
                } catch (StoreException exception) {
                    throw new RuntimeException(exception);
                }
            });
        } catch (JMSException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private void messageCreator(Message message) throws StoreException {
        if (message instanceof TextMessage textMessage) {
            try {
                saveEvent(textMessage.getText());
            } catch (JMSException exception) {
                throw new StoreException(exception.getMessage(), exception);
            }
        }
    }

    private void saveEvent(String event) throws StoreException {
        File file = fileOf(event);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
                fileWriter.write(event + "\n");
        } catch (IOException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private File fileOf(String event) throws StoreException {
        JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
        String tsNotFormatted = jsonObject.get("ts").getAsString();
        String ss = jsonObject.get("ss").getAsString();
        String ts = dateFormatter(tsNotFormatted);
        Path filePath = Paths.get(eventStoringPath, ss, ts + ".events");
        directoryCreator(filePath.getParent());
        return filePath.toFile();
        }

    private String dateFormatter(String tsNotFormatted) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDate localDateTs = LocalDate.parse(tsNotFormatted, dateTimeFormatter);
        return localDateTs.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private void directoryCreator(Path directoryPath) throws StoreException {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException exception) {
                throw new StoreException(exception.getMessage(), exception);
            }
        }
    }
}
