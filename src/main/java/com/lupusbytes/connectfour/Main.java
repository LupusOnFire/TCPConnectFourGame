package com.lupusbytes.connectfour;

import com.lupusbytes.connectfour.client.CommandlineClient;
import com.lupusbytes.connectfour.client.logic.controller.Controller;
import com.lupusbytes.connectfour.server.logic.ConnectionBroker;
import com.lupusbytes.connectfour.server.logic.Lobby;
import com.lupusbytes.connectfour.server.logic.entity.Board;
import com.lupusbytes.connectfour.server.logic.entity.Slot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //TODO use Apache commons to have more sane CLI controls
        if (args.length > 0) {
            if (args[0].equals("--com.lupusbytes.connectfour.server") || args[0].equals("-s")) {
                Integer serverPort = null;
                try {
                    serverPort = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.out.println("Missing com.lupusbytes.connectfour.server port integer");
                    return;
                }
                Lobby engine = new Lobby();
                ConnectionBroker connectionBroker = new ConnectionBroker(engine, serverPort);
                Thread connectionThread = new Thread(connectionBroker);
                connectionThread.start();
            } else if (args[0].equals("--com.lupusbytes.connectfour.client") || args[0].equals("-c")) {
                if (args.length > 1 && (args[1].equals("-h") || args[1].equals("--hotseat"))) {
                    hotSeat();
                } else {
                    Controller controller = new Controller();
                    CommandlineClient cli = new CommandlineClient(controller);
                    cli.setRemoteServer();
                    if (cli.joinServer()){
                        cli.clientInput();
                    }
                }
            }
        }
    }
    public static void hotSeat(){
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println(board.toString());
            System.out.print("Player1: ");
            String input = scanner.nextLine();
            int row = Integer.parseInt(input);
            board.putPiece(row, Slot.PLAYER1);
            System.out.println(board.toString());
            System.out.print("Player2: ");
            input = scanner.nextLine();
            row = Integer.parseInt(input);
            board.putPiece(row, Slot.PLAYER2);
        }
    }
}
