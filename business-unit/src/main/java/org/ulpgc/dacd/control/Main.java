package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

public class Main {

    public static void main(String[] args) throws StoreException {
        EventReceiver eventReceiver = new EventReceiver();
        JsonObject data = eventReceiver.messageReceiver();
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        eventAnalyzer.eventAnalyzer(data);


    }
}

//TODO cambiar el nombre????