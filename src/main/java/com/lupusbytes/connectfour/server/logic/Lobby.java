package com.lupusbytes.connectfour.server.logic;

import com.lupusbytes.connectfour.server.logic.entity.Client;
import com.lupusbytes.connectfour.server.logic.repository.ClientRepository;
import com.lupusbytes.connectfour.server.logic.service.MessengerService;
import com.lupusbytes.connectfour.server.logic.service.ProtocolService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;

import static com.lupusbytes.connectfour.server.logic.Constants.*;

public class Lobby implements Subject {
    ClientRepository clientRepository;
    ProtocolService protocolService;
    MessengerService messengerService;

    public Lobby() {
        this.clientRepository = new ClientRepository();
        this.protocolService = new ProtocolService(this);
        this.messengerService = new MessengerService(this);
    }

    @Override
    public void register(Socket socket, String username) {

        //if a client connects and user named is not taken
        if (!clientRepository.clientExists(username)) {
            Client client = clientRepository.addClient(socket, username, this);
            System.out.println(username + " has joined from " + socket.getRemoteSocketAddress());

            //send ack message to client
            messengerService.sendToClient(socket, J_OK);

            //send an updated client list to all observers
            notifyObserver(protocolService.getClientList(clientRepository.getClientsInLobby()));

            //start the client thread of async messages
            Thread thread = new Thread(client);
            thread.start();
        } else {
            messengerService.sendToClient(socket, JERR);
        }
    }

    public void reRegister(Client client){
        clientRepository.addClient(client);
        messengerService.sendToClient(client.getSocket(), J_OK);
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
        notifyObserver(protocolService.getClientList(clientRepository.getClientsInLobby()));
    }
    @Override
    public void unregister(Socket socket) {
        Client c = clientRepository.getClientBySocket(socket);
        clientRepository.removeClient(c);
        notifyObserver(protocolService.getClientList(clientRepository.getClientsInLobby()));
    }

    @Override
    public void notifyObserver(String message) {
        List<Client> clientList = clientRepository.getClientsInLobby();
        for (Client c : clientList) {
            messengerService.sendToClient(c.getSocket(), message);
        }
    }

    public void challengeClient(String challengerName, String opponentName) {
        Client client = clientRepository.getClientByUsername(opponentName);
        messengerService.sendToClient(client.getSocket(), GCHL + " " + challengerName);
    }
    public void challengeAccept(String challengerName, String opponentName) {
        Client challenger = clientRepository.getClientByUsername(challengerName);
        Client opponent = clientRepository.getClientByUsername(opponentName);

        messengerService.sendToClient(challenger.getSocket(), GACK + " " + opponentName);

        GameInstance gameInstance = new GameInstance(challenger, opponent);
        challenger.setInLobby(false);
        challenger.setGameInstance(gameInstance);
        opponent.setInLobby(false);
        opponent.setGameInstance(gameInstance);
        unregister(challengerName);
        unregister(opponentName);

    }
    public void challengeDecline(String challengerName, String opponentName) {
        Client client = clientRepository.getClientByUsername(challengerName);
        messengerService.sendToClient(client.getSocket(), GNAK + " " + opponentName);
    }

    public void digestMessage(String data) {
        protocolService.digestMessage(data);
    }
}
