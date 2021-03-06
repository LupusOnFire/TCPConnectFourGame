package com.lupusbytes.connectfour.client.cli;

import com.lupusbytes.connectfour.client.logic.IClient;
import com.lupusbytes.connectfour.client.logic.controller.Controller;
import com.lupusbytes.connectfour.client.logic.IProtocolInterpreter;

import java.net.InetAddress;
import java.util.Scanner;

public class CommandlineClient implements IClient {
    Controller controller;
    Scanner input;
    String username;
    IProtocolInterpreter interpreter;

    public CommandlineClient(Controller controller) {
        this.controller = controller;
        this.interpreter = new CliProtocolInterpreterImpl();
        this.input = new Scanner(System.in);
    }
    @Override
    public void setRemoteServer() {
        System.out.print("Username:\t\t");
        setUsername(input.nextLine());
        try {
            System.out.print("Server address:\t");
            InetAddress serverIp = InetAddress.getByName(input.nextLine());

            System.out.print("Server port:\t");
            int serverPort = Integer.parseInt(input.nextLine());

            controller.setProtocolInterpreter(interpreter);
            controller.setRemoteServer(serverIp, serverPort, this);


        } catch (Exception e) {
            System.out.println("Invalid server address");
        }
    }

    @Override
    public void displayData(String data) {
        System.out.println(data);
    }

    @Override
    public void clientInput() {
        System.out.println("Type \"!play 'name'\" to challenge a player, \"!accept 'name' to accept challenge and \"!decline 'player'\" to decline game");
        while (true) {
            String in = input.nextLine();
            controller.sendData(in);
            if (in.equals("!quit")){
                controller.stopConnectionThread();
                System.out.println("Goodbye!");
                return;
            }
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        interpreter.setUsername(username);
    }

    @Override
    public boolean joinServer()  {
        try {
            if (controller.joinServer(username)) {
                System.out.println("Successfully connected");
                return true;
            } else {
                System.out.println("Error connecting!");
            }
        } catch (NullPointerException e) {
            System.out.println("Could not connect to server");
            return false;
        }
        return false;
    }
}
