package server.logic;

import server.logic.entity.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Client implements Runnable{
    boolean isAlive;
    Engine engine;

    public ClientThread(Client client, Engine engine) {
        super(client.getSocket(), client.getUsername());
        this.engine = engine;
    }

    @Override
    public void run() {
        try {
            DataInputStream in;
            while (true) {
                in = new DataInputStream(getSocket().getInputStream());
                engine.digestMessage(in.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
