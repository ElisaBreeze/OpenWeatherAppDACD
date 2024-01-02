package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.List;

public interface EventStore extends AutoCloseable{
    void save(List<JsonObject> eventLists) throws SQLException, StoreException; //TODO manejar las excepciones!
}
