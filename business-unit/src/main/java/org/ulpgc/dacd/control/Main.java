package org.ulpgc.dacd.control;

public class Main {
    public static void main(String[] args) throws StoreException {
        EventReceiver eventReceiver = new EventReceiver();
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        EventController eventController = new EventController(eventReceiver, eventAnalyzer);
        eventController.manager(); //TODO cambiar nombre??
    }
}

//TODO acortar metodos a menos de 10 líneas