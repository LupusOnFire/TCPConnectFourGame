package com.lupusbytes.connectfour.server.logic;

import java.net.Socket;

public interface Subject {
    void register(Socket socket, String username);
    void unregister(String username);
    void unregister(Socket socket);
    void notifyObserver(String message);
}
