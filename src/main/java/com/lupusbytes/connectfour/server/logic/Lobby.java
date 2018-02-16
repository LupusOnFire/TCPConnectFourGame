package com.lupusbytes.connectfour.server.logic;

import com.lupusbytes.connectfour.server.logic.entity.Client;
import com.lupusbytes.connectfour.server.logic.repository.ClientRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static com.lupusbytes.connectfour.server.logic.Constants.*;

public class Lobby implements Subject {
    ClientRepository clientRepository;

    public Lobby() {
        this.clientRepository = new ClientRepository();
    }


    private String createListString(){
        String list = LIST + " ";
        List<Client> clientList = clientRepository.getClients();
        for (Client c : clientList) {
            if (c.isInLobby()) {
                list += c.getUsername() + " ";
            }
        }
        return list;
    }

    public void digestMessage(String message){
        System.out.println("RECEIVED: \"" + message + "\"");
        if (message.length() >= 4) {
            String header = message.substring(0, 4);
            switch (header) {
                case DATA: {
                    notifyObserver(message);
                    break;
                }
                case QUIT: {
                    quit(message.split(" ")[1]);
                    break;
                }
                case GCHL: {
                    String[] args = message.split(" ");
                    if (clientExists(args[2])) {
                        challengeClient(args[1], args[2]);
                    } else {
                        sendToClient(clientRepository.getClientByUsername(args[1]).getSocket(), GERR);
                    }
                    break;
                }
                case GACK: {
                    String[] args = message.split(" ");
                    challengeAccept(args[1], args[2]);
                    break;
                }
                case GNAK: {
                    String[] args = message.split(" ");
                    challengeDecline(args[1], args[2]);
                    break;
                }
            }
        }
    }

    @Override
    public void register(Socket socket, String username) {
        if (!clientExists(username)) {
            Client client = clientRepository.createClient(socket, username, this);
            System.out.println(username + " has joined from " + socket.getRemoteSocketAddress());
            sendToClient(socket, J_OK);
            notifyObserver(createListString());
            Thread thread = new Thread(client);
            thread.start();
        } else {
            sendToClient(socket, JERR);
        }
    }

    public void reRegister(Client client){
        clientRepository.addClient(client);
        sendToClient(client.getSocket(), J_OK);
    }

    public void quit(String username) {
        Client c = clientRepository.getClientByUsername(username);
        c.setAlive(false);
        try {
            c.getSocket().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        unregister(username);
    }

    @Override
    public void unregister(String username) {
            Client c = clientRepository.getClientByUsername(username);
            clientRepository.removeClient(c);
            notifyObserver(createListString());
    }

    @Override
    public void notifyObserver(String message) {
        List<Client> clientList = clientRepository.getClients();
        for (Client c : clientList) {
            if (c.isInLobby()) {
                sendToClient(c.getSocket(), message);
            }
        }
    }
    private boolean clientExists(String username) {
        Client client = clientRepository.getClientByUsername(username);
        if (client != null) {
            return true;
        }
        return false;
    }
    private void challengeClient(String challengerName, String opponentName) {
        Client client = clientRepository.getClientByUsername(opponentName);
        sendToClient(client.getSocket(), GCHL + " " + challengerName);
    }
    private void challengeAccept(String challengerName, String opponentName) {
        Client challenger = clientRepository.getClientByUsername(challengerName);
        Client opponent = clientRepository.getClientByUsername(opponentName);

        sendToClient(challenger.getSocket(), GACK + " " + opponentName);

        GameInstance gameInstance = new GameInstance(challenger, opponent);
        challenger.setInLobby(false);
        challenger.setGameInstance(gameInstance);
        opponent.setInLobby(false);
        opponent.setGameInstance(gameInstance);
        unregister(challengerName);
        unregister(opponentName);

    }
    private void challengeDecline(String challengerName, String opponentName) {
        Client client = clientRepository.getClientByUsername(challengerName);
        sendToClient(client.getSocket(), GNAK + " " + opponentName);
    }

    private void sendToClient(Socket socket, String message) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(message);
            System.out.println("SENDING: \"" + message + "\"");
        } catch (IOException e) {
            unregister(clientRepository.getClientBySocket(socket).getUsername());
        }
    }
}
