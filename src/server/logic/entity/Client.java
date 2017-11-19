package server.logic.entity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Observer {
    private String username;
    Socket socket;
    boolean inLobby;

    public Client(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
        this.inLobby = true;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isInLobby() {
        return inLobby;
    }

    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
    }

    @Override
    public void update(String message) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
