package server.logic;

import server.logic.entity.Client;
import server.logic.repository.ClientRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static server.logic.Constants.*;

public class Engine implements Subject, Runnable {
    ClientRepository clientRepository;

    public Engine() {
        this.clientRepository = new ClientRepository();
    }           //System.out.println(in);


    private String createListString(){
        String list = LIST + " ";
        List<Client> clientList = clientRepository.getClients();
        for (Client c : clientList) {
            list += c.getUsername() + " ";
        }
        return list;
    }

    public void digestMessage(String message){
        System.out.println("SERVER RECEIVED: \"" + message + "\"");
        if (message.length() >= 4) {
            String header = message.substring(0, 4);
            switch (header) {
                case DATA: {
                    notifyObserver(message);
                    break;
                }
                case QUIT: {
                    unregister(message);
                    break;
                }
                case GCHL: {
                    String[] args = message.split(" ");
                    if (clientExists(args[3])) {
                        challengeClient(args[2], args[3]);
                    } else {
                        sendToClient(clientRepository.getClientByUsername(args[2]).getSocket(), GERR);
                    }
                    break;
                }
                case GACK: {
                    String[] args = message.split(" ");
                    challengeAccept(args[2], args[3]);
                    break;
                }
                case GNAK: {
                    String[] args = message.split(" ");
                    challengeDecline(args[2], args[3]);
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
    }

    @Override
    public void register(Socket socket, String username) {
        if (!clientExists(username)) {
            Client client = clientRepository.addClient(socket, username);
            System.out.println(username + " has joined");
            sendToClient(socket, J_OK);
            notifyObserver(createListString());
            ClientThread clientThread = new ClientThread(client, this);
            Thread thread = new Thread(clientThread);
            thread.start();
        } else {
            sendToClient(socket, JERR);
        }
    }

    @Override
    public void unregister(String username) {
        try {
            Client c = clientRepository.getClientByUsername(username);
            c.getSocket().close();
            clientRepository.removeClient(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObserver(String message) {
        List<Client> clientList = clientRepository.getClients();
        for (Client c : clientList) {
            sendToClient(c.getSocket(), message);
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
        Client client = clientRepository.getClientByUsername(challengerName);
        sendToClient(client.getSocket(), GACK + " " + opponentName);
    }
    private void challengeDecline(String challengerName, String opponentName) {
        Client client = clientRepository.getClientByUsername(challengerName);
        sendToClient(client.getSocket(), GNAK + " " + opponentName);
    }

    private void sendToClient(Socket socket, String message) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(message);
            System.out.println("SERVER SENDING: \"" + message + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
