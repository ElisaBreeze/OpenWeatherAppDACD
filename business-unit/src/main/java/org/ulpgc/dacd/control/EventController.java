package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EventController {
    private final EventReceiver eventReceiver;
    private final EventAnalyzer eventAnalyzer;
    private final InformationCreator informationCreator;
    private final InformationDisplay informationDisplay;
    private final SQLDataStoreManager eventStore;

    public EventController(EventReceiver eventReceiver, EventAnalyzer eventAnalyzer, InformationCreator informationCreator, InformationDisplay informationDisplay, SQLDataStoreManager eventStore){
        this.eventAnalyzer = eventAnalyzer;
        this.eventReceiver = eventReceiver;
        this.informationCreator = informationCreator; //TODO borrar informationCreator una vez q fx funciona
        this.informationDisplay = informationDisplay;
        this.eventStore = eventStore;
    }

    public void manager() throws StoreException {
        System.out.println("You want to go on a little trip on the Canary Islands in the next 5 days? \n Wait and see what your best options are!");
        System.out.println("Receiving data - this might take a moment");
        System.out.println("Please take into account that some hotels might not be available, only the available ones will be analyzed.");
        List<JsonObject> eventLists = eventReceiver.messageReceiver();
        System.out.println("Data Received correctly, analyzing the options");
        eventStore.save(eventLists);

        //analizo pal cleinte desde datamart o memoria?
        //TODO Tirar del datalake pal primer dato? y lgo seguir con real time => creo que no hace falta
        Map<String, JsonObject> finalBestOptions = eventAnalyzer.bestOptions(eventLists);
        informationCreator.printInformation(finalBestOptions);

        informationDisplay.display(finalBestOptions);
        }
}
