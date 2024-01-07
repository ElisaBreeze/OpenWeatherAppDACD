package org.ulpgc.dacd.control;
public class Main {
    public static void main(String[] args) throws StoreException{
        EventReceiver eventReceiver = new EventReceiver();
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        InformationDisplayer informationDisplayer = new InformationDisplayer();
        SQLDataStoreManager eventStore = new SQLDataStoreManager();
        EventController eventController = new EventController(eventReceiver, eventAnalyzer, informationDisplayer, eventStore);
        eventController.manager();
    }
}
