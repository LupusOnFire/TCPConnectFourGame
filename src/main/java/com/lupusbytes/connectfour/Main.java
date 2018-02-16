package com.lupusbytes.connectfour;

import com.lupusbytes.connectfour.client.cli.CommandlineClient;
import com.lupusbytes.connectfour.client.logic.IClient;
import com.lupusbytes.connectfour.client.logic.controller.Controller;
import com.lupusbytes.connectfour.server.logic.ConnectionBroker;
import com.lupusbytes.connectfour.server.logic.Lobby;
import com.lupusbytes.connectfour.server.logic.entity.Board;
import com.lupusbytes.connectfour.server.logic.entity.Slot;
import org.apache.commons.cli.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int port = 32450;

        try {
            Options options = new Options();
            options.addOption("s", "server",false, "Start server");
            options.addOption("c", "client",false, "Start client");
            options.addOption("h","headless",false, "Start headless client");
            options.addOption("p", "port",true, "Port");
            CommandLineParser cmdParser = new GnuParser();
            CommandLine cmd = cmdParser.parse(options, args);

            if (cmd.hasOption("p"))
                port = Integer.parseInt(cmd.getOptionValue("p"));

            // Start client or server, never more than one
            if (cmd.hasOption("c") && !cmd.hasOption("h")) {
                System.out.println("JavaFX client started");
            } else if (cmd.hasOption("h")) {
                System.out.println("CLI client starting");
            } else if (cmd.hasOption("s")) {
                System.out.println("Server starting");
                startServer(port);
            }
        } catch (ParseException e) {
            System.out.println("Parsing failed. Reason: " + e.getMessage());
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
    public static void startServer(int port) {
        Lobby engine = new Lobby();
        ConnectionBroker connectionBroker = new ConnectionBroker(engine, port);
        Thread connectionThread = new Thread(connectionBroker);
        connectionThread.start();
    }

    public static void startGuiClient() {

    }

    public static void startCliClient() {
        Controller controller = new Controller();
        IClient cliClient = new CommandlineClient(controller);
        cliClient.setRemoteServer();
        if (cliClient.joinServer()){
            cliClient.clientInput();
        }
    }
}
