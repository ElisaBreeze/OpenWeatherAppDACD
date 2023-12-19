package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Price;

import javax.jms.*;
import java.time.Instant;

public class JMSHotelDataStore implements PriceStore {
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;

    @Override
    public void save(Price price) throws StoreException {
        try {
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topicName = session.createTopic("hotel.information");
            MessageProducer messageProducer = session.createProducer(topicName);
            TextMessage textMessage = messageCreator(session, price);
            messageProducer.send(textMessage);
            connection.close();
        } catch (JMSException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private TextMessage messageCreator(Session session, Price price) throws StoreException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeAdapter()).create();
        try {
            return session.createTextMessage(gson.toJson(price));
        } catch (JMSException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    @Override
    public void close(){}
}
