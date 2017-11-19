package client.logic;

import client.logic.entity.GameServer;

import java.io.*;
import java.net.Socket;

import static server.logic.Constants.*;

public class ConnectionSocket implements Runnable {
    private GameServer gameServer;
    Socket clientSocket;

    public ConnectionSocket(GameServer gameServer) throws IOException {
        this.gameServer = gameServer;
        this.clientSocket = new Socket(gameServer.getIp(), gameServer.getPort());
    }


    public void sendMessage(String message) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeUTF(message);
    }

    //TODO use a better pattern so it can also be implemented in a GUI
    public String digestMessage(String message) {
        if (message.length() >= 4) {
            String header = message.substring(0, 4);
            switch (header) {
                case J_OK: {
                    return "Successfully connected to " + clientSocket.getRemoteSocketAddress();
                }
                case JERR: {
                    System.out.println("Username not available, aborting");
                    try {
                        clientSocket.close();
                    } catch (Exception e) {
                        System.out.println("Error closing connection");
                    }
                    System.exit(0);
                }
                case LIST: {
                    String out = "Clients in lobby: ";
                    String[] clients = message.split(" ");
                    for (int i = 1; i < clients.length; i++) {
                        out += (clients[i] + " ");
                    }
                    return out;
                }
                case DATA: {
                    return message.substring(5, message.length());

                }
            }
        }
        return "";
    }

    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                System.out.println(digestMessage(in.readUTF()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
