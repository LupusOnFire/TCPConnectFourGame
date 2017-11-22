package server.logic.repository;

import server.logic.Lobby;
import server.logic.entity.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    List<Client> clients;

    public ClientRepository() {
        clients = new ArrayList<>();
    }

    public Client createClient(Socket socket, String username, Lobby engine){
        Client client = new Client(socket, username, engine);
        clients.add(client);
        return client;
    }

    public void addClient(Client client) {
        clients.add(client);
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
