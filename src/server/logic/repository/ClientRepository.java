package server.logic.repository;

import server.logic.entity.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    List<Client> clients;

    public ClientRepository() {
        clients = new ArrayList<>();
    }

    public Client addClient(Socket socket, String username){
        Client client = new Client(socket, username);
        clients.add(client);
        return client;
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getClientByUsername(String username) {
        for(Client c : clients) {
            if (username.equals(c.getUsername())) {
                return c;
            }
        }
        return null;
    }
    public Client getClientBySocket(Socket socket) {
        for (Client c : clients) {
            if (c.getSocket() == socket) {
                return c;
            }
        }
        return null;
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }
}
