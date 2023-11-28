package org.ulpgc.dacd.control;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.jms.*;
import java.time.Instant;

public class JMSWeatherStore implements WeatherStore {
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final String topic = "prediction.Weather";

    @Override
    public void save(Weather weather) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
        connection.start();

        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic(topic);

        MessageProducer messageProducer = session.createProducer(destination);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        ObjectMessage textMessage = session.createObjectMessage(gson.toJson(weather));
        messageProducer.send(textMessage);

        connection.close();
    }

    @Override
    public void close(){
    }
}
