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
//TODO revisar en todas las clases las excepciones
//TODO ver porq se guardan tantos eventos del mismo sitio
//datalake-builder y ver si va bien
//cambiar nombre x-business-unit
//TODO no olvidarme del release
//TODO update readme y UML!
//TODO en readme poner que los datos del precio tardan algo más en salir y que se espera.
//TODO ORDENAR TODO; CONTROLLER Y TODO
//TODO acortar metodos a menos de 10 líneas