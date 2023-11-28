package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.control.InstantTypeAdapter;
import org.ulpgc.dacd.model.Weather;
import javax.jms.*;
import java.time.Instant;

public class WeatherEventReceiver {
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final String topic = "prediction.Weather";

    public static void main(String[] args) {
        try {
            new WeatherEventReceiver().messageReceiver();
        } catch (JMSException exception) {
            throw new RuntimeException();
        }
    }

    public void messageReceiver() throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
        connection.start();

        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic(topic);
        MessageConsumer consumer = session.createConsumer(destination);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        while(true){
            Message message = consumer.receive();
            if (message instanceof ObjectMessage objectMessage) {
                String json = (String) objectMessage.getObject();
                Weather weather = gson.fromJson(json, Weather.class);
                System.out.println("Received Weather object:" + weather);
            }
        }
    }
}
