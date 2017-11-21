package server.logic.entity;

import server.logic.GameInstance;
import server.logic.Lobby;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private String username;
    private Socket socket;
    private boolean inLobby, isAlive;
    private GameInstance gameInstance;
    private Lobby lobby;

    public Client(Socket socket, String username, Lobby lobby) {
        this.socket = socket;
        this.username = username;
        this.lobby = lobby;
        this.isAlive = true;
        this.inLobby = true;
    }

    public void setGameInstance(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
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

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public void run() {
        try {
            DataInputStream in;
            while (isAlive) {
                in = new DataInputStream(getSocket().getInputStream());
                if (isInLobby()) {
                    lobby.digestMessage(in.readUTF());
                } else {
                    System.out.println("sending data to gameinstance");
                    gameInstance.digestMessage(in.readUTF());
                }
            }
        } catch (IOException e) {
            lobby.quit(username);
        }
    }
}
