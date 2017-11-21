package client.logic.controller;
import client.IClient;
import client.logic.service.*;
import server.logic.entity.Client;

import java.io.IOException;
import java.net.InetAddress;

public class Controller {
    private IProtocolInterpreter interpreter;
    private ConnectionService connectionService;

    public Controller() {
    }

    public void setRemoteServer(InetAddress serverAddress, int serverPort, IClient client) throws IOException {
        this.connectionService = new ConnectionService(serverAddress, serverPort, getInterpreter(), client);
    }
    public void setProtocolInterpreter(IProtocolInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    public IProtocolInterpreter getInterpreter() {
        return interpreter;
    }
    public boolean joinServer(String username) {
        boolean connected = connectionService.joinServer(username);
        if (connected) {
            startConnectionThread();
        }
        return connected;
    }
    private void startConnectionThread() {
        Thread thread = new Thread(connectionService);
        thread.start();
    }
    public void stopConnectionThread() {
        connectionService.setAlive(false);
    }
    public void sendData(String data) {
        connectionService.sendData(data);
    }
}
