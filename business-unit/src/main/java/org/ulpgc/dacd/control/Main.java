package org.ulpgc.dacd.control;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws StoreException{
        EventReceiver eventReceiver = new EventReceiver();
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        InformationCreator informationCreator = new InformationCreator();
        InformationDisplay informationDisplay = new InformationDisplay();
        SQLDataStoreManager eventStore = new SQLDataStoreManager();
        EventController eventController = new EventController(eventReceiver, eventAnalyzer, informationCreator,informationDisplay, eventStore);
        eventController.manager();

    }
}
//TODO update readme y UML!
//TODO ORDENAR TODO; CONTROLLER Y TODO => HACER INTERFACES??
//TODO acortar metodos a menos de 10 líneas