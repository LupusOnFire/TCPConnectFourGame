package com.lupusbytes.connectfour.server.logic.service;

import com.lupusbytes.connectfour.server.logic.Lobby;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MessengerService {
    private Lobby lobby;
    public MessengerService(Lobby lobby) {
        this.lobby = lobby;
    }
    public void sendToClient(Socket socket, String message) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(message);
            System.out.println("SENDING: \"" + message + "\"");
        } catch (IOException e) {
            lobby.unregister(socket);
        }
    }
}
