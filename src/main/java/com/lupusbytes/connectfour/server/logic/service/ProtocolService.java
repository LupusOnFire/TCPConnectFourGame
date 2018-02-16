package com.lupusbytes.connectfour.server.logic.service;

import com.lupusbytes.connectfour.server.logic.Lobby;
import com.lupusbytes.connectfour.server.logic.entity.Client;
import com.lupusbytes.connectfour.server.logic.repository.ClientRepository;

import java.util.List;


import static com.lupusbytes.connectfour.server.logic.Constants.*;

public class ProtocolService {
    private Lobby lobby;

    public ProtocolService(Lobby lobby) {
        this.lobby = lobby;
    }

    public void digestMessage(String message){
        System.out.println("RECEIVED: \"" + message + "\"");

        if (message.length() >= 4) {
            String header = message.substring(0, 4);
            switch (header) {
                case DATA: {
                    lobby.notifyObserver(message);
                    break;
                }
                case QUIT: {
                    lobby.quit(message.split(" ")[1]);
                    break;
                }
                case GCHL: {
                    String[] args = message.split(" ");
                    lobby.challengeClient(args[1], args[2]);
                    break;
                }
                case GACK: {
                    String[] args = message.split(" ");
                    lobby.challengeAccept(args[1], args[2]);
                    break;
                }
                case GNAK: {
                    String[] args = message.split(" ");
                    lobby.challengeDecline(args[1], args[2]);
                    break;
                }
            }
        }
    }


    public String getClientList(List<Client> clients){
        StringBuilder stringBuilder = new StringBuilder(LIST + " ");
        for (Client c : clients) {
            stringBuilder.append(c.getUsername() + " ");
        }
        return stringBuilder.toString();
    }
}
