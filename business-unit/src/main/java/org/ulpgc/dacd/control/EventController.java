package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;
import java.util.List;

public class EventController {
    private final EventReceiver eventReceiver;
    private final EventAnalyzer eventAnalyzer;

    public EventController(EventReceiver eventReceiver, EventAnalyzer eventAnalyzer){
        this.eventAnalyzer = eventAnalyzer;
        this.eventReceiver = eventReceiver;
    }

    public void manager() throws StoreException {
        System.out.println("Receiving data");
        List<JsonObject> eventLists = eventReceiver.messageReceiver();
        System.out.println("Data Received, analyzing the options");

        //TODO Tirar del datalake pal primer dato? y lgo seguir con real time => cómo
        eventAnalyzer.bestOptions(eventLists); //TODO como hago pa que me devuelve las mejores? o lo impirmo en el otro directamente??
        //TODO aqui llamar al save pal datamart
        //TODO aqui imprimir pal cliente?? => When hotel is chosen, give back all information on weather and price for best options. => in clientInformation or something like that
        }
    }
