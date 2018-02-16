package com.lupusbytes.connectfour.server.logic;

import java.net.Socket;

public interface Subject {
    void register(Socket socket, String username);
    void unregister(String username);
    void notifyObserver(String message);
}
