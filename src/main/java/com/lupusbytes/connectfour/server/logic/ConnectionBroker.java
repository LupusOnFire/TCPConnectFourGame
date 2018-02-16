package com.lupusbytes.connectfour.server.logic;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import static com.lupusbytes.connectfour.server.logic.Constants.JERR;

public class ConnectionBroker implements Runnable{
    boolean stopped = false;
    private int serverPort;
    ServerSocket serverSocket;
    Lobby lobby;

    public ConnectionBroker(Lobby lobby, int serverPort) {
        this.serverPort = serverPort;
        this.lobby = lobby;
    }

    @Override
    public void run() {
        //start listening on the port
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println(new Date().toString() + " Listening for connections on port " + serverPort);
        } catch (IOException e) {
            System.out.println("Cannot start ServerSocket on port " + serverPort);
            return;
        }

        while (!stopped) {
            try {
                //accept incoming connections
                Socket newSocket = serverSocket.accept();

                //get data
                DataInputStream in = new DataInputStream(newSocket.getInputStream());
                String data = DataInputStream.readUTF(in);

                //prepare to answer
                DataOutputStream out = new DataOutputStream(newSocket.getOutputStream());

                //accept and add client if client uses the protocol
                if (data.startsWith("JOIN ") && data.length() > 5) {
                    lobby.register(newSocket, data.substring(5, data.length()));
                } else {
                    out.writeUTF(JERR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
