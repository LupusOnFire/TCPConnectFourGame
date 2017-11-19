package server.logic;

import server.logic.entity.Client;

import java.net.DatagramPacket;
import java.net.Socket;

public interface Subject {
    boolean register(Socket socket, String username);
    void unregister(String username);
    void notifyObserver(String message);
}
