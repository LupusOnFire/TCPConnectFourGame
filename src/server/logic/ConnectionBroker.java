package server.logic;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ConnectionBroker implements Runnable{
    boolean stopped = false;
    private int serverPort;
    ServerSocket serverSocket;
    Engine engine;

    public ConnectionBroker(Engine engine, int serverPort) {
        this.serverPort = serverPort;
        this.engine = engine;
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
                    //Engine.register returns true or false depending on if username is available
                    if (engine.register(newSocket, data.substring(5, data.length()))) {
                        out.writeUTF(Constants.J_OK);
                    }
                } else {
                    out.writeUTF(Constants.JERR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}