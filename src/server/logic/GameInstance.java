package server.logic;

import server.logic.entity.Board;
import server.logic.entity.Client;
import server.logic.entity.Slot;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static server.logic.Constants.DATA;
import static server.logic.Constants.GAME;
import static server.logic.Constants.MOVE;

public class GameInstance {
    private Board board;
    private Client player1, player2;

    public GameInstance(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board();
        sendToBothClients(board.toString());
        System.out.println("BOARD:\n" + board.toString());
    }

    public Client getPlayer1() {
        return player1;
    }
    public Client getPlayer2() {
        return player2;
    }

    public void digestMessage(String data) {
        if (data.length() >= 4) {
            String header = data.substring(0, 4);
            String[] args = data.split(" ");
            switch (header) {
                case DATA: {
                    sendToBothClients(data);
                    break;
                }
                case MOVE: {
                    int row = Integer.parseInt(args[3]);
                    if (args[1].equals(player1.getUsername())) {
                        board.putPiece(row, Slot.PLAYER1);
                    } else  {
                        board.putPiece(row, Slot.PLAYER2);
                    }
                    sendToBothClients(GAME + " " + board.toString());
                    break;
                }
                default: {
                    sendToBothClients(data);
                    break;
                }
            }
        }
    }
    public void gameObjectStream(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(player1.getSocket().getOutputStream());
            out.writeObject(board);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendToBothClients(String data){
        try {
            DataOutputStream out = new DataOutputStream(player1.getSocket().getOutputStream());
            out.writeUTF(data);
            out = new DataOutputStream(player2.getSocket().getOutputStream());
            out.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

