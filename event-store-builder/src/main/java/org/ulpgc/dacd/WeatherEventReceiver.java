package org.ulpgc.dacd;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.control.StoreExceptions; //TODO check si se puede hacer así (con dependencia)
import org.ulpgc.dacd.model.Weather; //TODO lo mismo que la linea anterior
import javax.jms.*;

public class WeatherEventReceiver {
    private final String serverURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final String topic = "prediction.Weather";

    public static void main(String[] args) {
        try {
            new WeatherEventReceiver().messageReceiver();
        } catch (StoreExceptions exception) {
            throw new RuntimeException(exception); //TODO bien??
        }
    }

    public void messageReceiver() throws StoreExceptions {
        try {
            Connection connection = new ActiveMQConnectionFactory(serverURL).createConnection();
            connection.start();

            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(topic);
            MessageConsumer consumer = session.createConsumer(destination);

            while (true) {
                Message message = consumer.receive();
                if (message instanceof ObjectMessage objectMessage) {
                    Weather weather = (Weather) objectMessage.getObject();
                    System.out.println("Received Weather object:" + weather);
                }
            }
        } catch (JMSException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }

    }
}
