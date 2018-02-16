package com.lupusbytes.connectfour.server.logic.repository;

import com.lupusbytes.connectfour.server.logic.Lobby;
import com.lupusbytes.connectfour.server.logic.entity.Client;

import java.net.Socket;
import java.util.*;

public class ClientRepository {
    Map<String, Client> clients;

    public ClientRepository() {
        clients = new HashMap();
    }

    public Client newClient(Socket socket, String username, Lobby engine){
        Client client = new Client(socket, username, engine);
        clients.put(username, client);
        return client;
    }

    public void addClient(Client client) {
        if (!clientExists(client.getUsername()))
            clients.put(client.getUsername(), client);
    }

    public boolean clientExists(String username) {
        if (clients.containsKey(username)) {
            return true;
        }
        return false;
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public List getClientsInLobby() {
        List<Client> clientsInLobby = new LinkedList<>();
        for (Map.Entry<String, Client> c : clients.entrySet()) {
            if (c.getValue().isInLobby() == true) {
                clientsInLobby.add(c.getValue());
            }
        }
        return clientsInLobby;
    }

    public Client getClientByUsername(String username) {
        if (clients.containsKey(username)) {
            return clients.get(username);
        }
        return null;
    }
    public Client getClientBySocket(Socket socket) {
        for (Map.Entry<String, Client> c : clients.entrySet()) {
            if (c.getValue().getSocket() == socket) {
                return c.getValue();
            }
        }
        return null;
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }
}
