package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public class EventController {
    private final EventReceiver eventReceiver;
    private final EventAnalyzer eventAnalyzer;
    private final InformationDisplayer informationDisplayer;
    private final SQLDataStoreManager eventStore;

    public EventController(EventReceiver eventReceiver, EventAnalyzer eventAnalyzer, InformationDisplayer informationDisplayer, SQLDataStoreManager eventStore){
        this.eventAnalyzer = eventAnalyzer;
        this.eventReceiver = eventReceiver;
        this.informationDisplayer = informationDisplayer;
        this.eventStore = eventStore;
    }

    public void manager() throws StoreException {
        List<JsonObject> eventLists = eventReceiver.messageReceiver();
        eventStore.save(eventLists);

        Map<String, JsonObject> finalBestOptions = eventAnalyzer.bestOptions(eventLists);
        eventStore.saveBestOptions(finalBestOptions);
        informationDisplayer.displayer();
        }
}
