package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Price;

import java.util.List;

public interface PriceProvider {
    List<Price> getPrice(Hotel hotel) throws StoreException;
}
