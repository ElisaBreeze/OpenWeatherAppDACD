package org.example;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class WeatherEventReceiver implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {

        }
    }
}
