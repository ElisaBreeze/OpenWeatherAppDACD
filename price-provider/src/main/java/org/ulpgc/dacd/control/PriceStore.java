package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Price;
public interface PriceStore extends AutoCloseable{
    void save(Price price) throws StoreException;
}
