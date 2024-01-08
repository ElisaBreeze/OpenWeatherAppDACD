package org.ulpgc.dacd;

public class Main {
    public static void main(String[] args) throws StoreException {
        EventReceiver eventReceiver = new EventReceiver(args[0]);
        eventReceiver.messageReceiver();
    }
}
