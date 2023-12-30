package org.ulpgc.dacd.control;

public class Main {
    public static void main(String[] args) throws StoreException {
        EventReceiver eventReceiver = new EventReceiver();
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        InformationCreator informationCreator = new InformationCreator();
        EventController eventController = new EventController(eventReceiver, eventAnalyzer, informationCreator);
        eventController.manager(); //TODO cambiar nombre??
    }
}

//TODO acortar metodos a menos de 10 líneas