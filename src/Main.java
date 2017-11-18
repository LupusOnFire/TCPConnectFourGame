import logic.Board;
import logic.Slot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
