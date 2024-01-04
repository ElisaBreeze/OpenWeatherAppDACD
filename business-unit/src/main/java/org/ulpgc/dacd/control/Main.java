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
//TODO check why there is concurrenterror sometimes
//FALTA ACORTAR 1 metodo en analyzer
//TODO ver porq se guardan tantos eventos del mismo sitio
//datalake-builder y ver si va bien
//cambiar nombre x-business-unit
//TODO no olvidarme del release
//TODO update readme y UML!
//TODO en readme poner que los datos del precio tardan algo más en salir y que se espera con el countdownlatch con el numero de eventos que van a llegar
//TODO en readme explicar que cojo por prioridad datos booking, lgo hotelcom lgo cualquiera
//TODO ORDENAR TODO; CONTROLLER Y TODO
//TODO acortar metodos a menos de 10 líneas