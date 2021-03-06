package com.lupusbytes.connectfour.client.logic.service;

import com.lupusbytes.connectfour.client.logic.IClient;
import com.lupusbytes.connectfour.client.logic.IProtocolInterpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static com.lupusbytes.connectfour.server.logic.Constants.JOIN;
import static com.lupusbytes.connectfour.server.logic.Constants.J_OK;

public class ConnectionService implements Runnable {
    private IProtocolInterpreter protocolInterpreter;
    private Socket clientSocket;
    private IClient client;
    private boolean isAlive;

    public ConnectionService(InetAddress serverAddress, int serverPort, IProtocolInterpreter protocolInterpreter, IClient client) throws IOException {
        this.clientSocket = new Socket(serverAddress, serverPort);
        this.protocolInterpreter = protocolInterpreter;
        this.client = client;
    }

    public boolean joinServer(String username) {
        try {
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF(JOIN + " " + username);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            if (in.readUTF().equals(J_OK)) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public void sendData(String data) {
        try {
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF(protocolInterpreter.output(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public void run() {
        this.isAlive = true;
        while (isAlive) {
            try {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                client.displayData(protocolInterpreter.input(in.readUTF()));
            } catch (IOException e) {
                isAlive = false;
            }
        }
    }
}
