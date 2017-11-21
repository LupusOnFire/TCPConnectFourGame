import client.CommandlineClient;
import client.logic.controller.Controller;
import client.logic.service.CliProtocolInterpreterImpl;
import client.logic.service.IProtocolInterpreter;
import server.logic.ConnectionBroker;
import server.logic.Engine;
import server.logic.entity.Board;
import server.logic.entity.Slot;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //TODO use Apache commons to have more sane CLI controls
        if (args.length > 0) {
            if (args[0].equals("--server") || args[0].equals("-s")) {
                Integer serverPort = null;
                try {
                    serverPort = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.out.println("Missing server port integer");
                    return;
                }
                Engine engine = new Engine();
                ConnectionBroker connectionBroker = new ConnectionBroker(engine, serverPort);
                Thread connectionThread = new Thread(connectionBroker);
                connectionThread.start();
            } else if (args[0].equals("--client") || args[0].equals("-c")) {
                if (args.length > 1 && (args[1].equals("-h") || args[1].equals("--hotseat"))) {
                    hotSeat();
                } else {
                    Controller controller = new Controller();
                    CommandlineClient cli = new CommandlineClient(controller);
                    cli.setRemoteServer();
                    cli.joinServer();
                    cli.clientInput();
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
