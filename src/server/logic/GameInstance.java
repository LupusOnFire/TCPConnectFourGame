package server.logic;

import server.logic.entity.Board;
import server.logic.entity.Client;
import server.logic.entity.Slot;

import java.io.ByteArrayOutputStream;
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
        sendToBothClients(GAME + " " + serialize(board));
        System.out.println(board.toString());
    }

    public Client getPlayer1() {
        return player1;
    }
    public Client getPlayer2() {
        return player2;
    }

    public static String serialize(Object obj) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(obj);
            so.flush();
            // This encoding induces a bijection between byte[] and String (unlike UTF-8)
            return bo.toString("ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
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
                /*case MOVE: {
                    int row = Integer.parseInt(args[3]);
                    if (args[1].equals(player1.getUsername())) {
                        board.putPiece(row, Slot.PLAYER1);
                    } else  {
                        board.putPiece(row, Slot.PLAYER2);
                    }
                    sendToBothClients(GAME + " " + board.toString());
                    break;
                }*/
                default: {
                    sendToBothClients(data);
                    break;
                }
            }
        }
    }
    public String serializeBoard() {
        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(board);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return serializedObject;
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

