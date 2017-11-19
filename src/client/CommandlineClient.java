package client;

import client.logic.ConnectionSocket;
import client.logic.entity.GameServer;
import server.logic.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class CommandlineClient {
    Scanner input;
    String username;
    ConnectionSocket connectionSocket;

    public CommandlineClient() {
        input = new Scanner(System.in);
    }
    public void connectToServer() {
        System.out.print("Username:\t\t");
        setUsername(input.nextLine());
        try {
            System.out.print("Server address:\t");
            InetAddress serverIp = InetAddress.getByName(input.nextLine());
            System.out.print("Server port:\t");
            int serverPort = Integer.parseInt(input.nextLine());
            this.connectionSocket = new ConnectionSocket(new GameServer(serverIp, serverPort));
            connectionSocket.sendMessage(Constants.JOIN + " " + username);
            Thread connectionThread = new Thread(connectionSocket);
            connectionThread.start();
            handleClientInput();
        } catch (Exception e) {
            System.out.println("Invalid server address");
        }
    }

    public void handleClientInput() {
        while (true) {
            try {
                String in = input.nextLine();
                switch (in.toLowerCase()) {
                    case "play": {
                        connectionSocket.sendMessage(Constants.GCHL + " " + in.split(" ")[1]);
                        break;
                    }
                    default: {
                        connectionSocket.sendMessage("DATA " + username + ": " + in);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
