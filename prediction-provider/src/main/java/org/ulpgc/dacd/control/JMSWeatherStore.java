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

    @Override
    public void save(Weather weather) throws StoreException {
        try {
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topicName = session.createTopic("prediction.Weather");
            MessageProducer messageProducer = session.createProducer(topicName);
            TextMessage textMessage = messageCreator(session, weather);
            messageProducer.send(textMessage);
            connection.close();
        } catch (JMSException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private TextMessage messageCreator(Session session, Weather weather) throws StoreException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeAdapter()).create();
        try {
            return session.createTextMessage(gson.toJson(weather));
        } catch (JMSException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    @Override
    public void close(){}
}
