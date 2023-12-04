package org.ulpgc.dacd;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.control.StoreExceptions;
import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination topicDestination = session.createTopic(topic);
            MessageConsumer consumer = session.createConsumer(topicDestination);
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
        List<String> events = new ArrayList<>();
        if (message instanceof TextMessage textMessage) {
            try {
                String weatherData = textMessage.getText();
                events.add(weatherData);
                saveEvent(eventStoringPath, events);
                System.out.println("Datos: " + weatherData);
            } catch (JMSException exception) {
                throw new StoreExceptions(exception.getMessage(), exception);
            }
        }
    }

    private void saveEvent(String eventStoringPath, List events) throws StoreExceptions {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = LocalDate.now().format(dateTimeFormatter);
        Path eventStorer = Paths.get(eventStoringPath, "prediction.Weather", ss, formattedDate + ".events");
        directoryCreator(eventStorer.getParent());
        try (FileWriter fileWriter = new FileWriter(eventStorer.toString(), true)) {
            for (Object data : events){
                fileWriter.write(data.toString() + "\n");
            }
            events.clear(); //TODO quitar si no va
        } catch (IOException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
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
