package server.logic;

import server.logic.entity.Board;
import server.logic.entity.Client;
import server.logic.entity.Slot;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;

import static server.logic.Constants.*;

public class GameInstance {
    private Board board;
    private Client player1, player2, turn;

    public GameInstance(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board();
        this.turn = player1;
        sendToBothClients(GAME + " " + board.toString());
        sendToBothClients(TURN + " " + turn.getUsername());
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
                case MOVE: {
                    int row = Integer.parseInt(args[2]);
                    if (turn.getUsername().equals(args[1])) {
                        if (player1.getUsername().equals(args[1])) {
                            //check if player has won
                            boolean hasWon = board.putPiece(row, Slot.PLAYER1);
                            if (hasWon) {
                                sendToBothClients(GAME + " " + board.toString());
                                win(player1);
                                break;
                            }
                            turn = player2;
                        } else {
                            boolean hasWon = board.putPiece(row, Slot.PLAYER2);
                            if (hasWon) {
                                sendToBothClients(GAME + " " + board.toString());
                                win(player2);
                                break;
                            }
                            turn = player1;
                        }
                        if (!player1.getUsername().equals(args[1])) {
                            sendToClient(player1, GERR);
                        } else {
                            sendToClient(player2, GERR);
                        }
                    }
                    sendToBothClients(GAME + " " + board.toString());
                    sendToBothClients(TURN + " " + turn.getUsername());
                    break;
                }
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

    public void sendToClient(Client client, String data) {
        try {
            DataOutputStream out = new DataOutputStream(client.getSocket().getOutputStream());
            out.writeUTF(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToBothClients(String data){
        sendToClient(player1, data);
        sendToClient(player2, data);
    }

    public void win(Client winner) {
        sendToBothClients(GWIN + " " + winner.getUsername());
        returnToLobby();
    }

    public void returnToLobby() {
        player1.setInLobby(true);
        player2.setInLobby(true);
        player1.setGameInstance(null);
        player2.setGameInstance(null);
        System.out.println(player1.isInLobby());
    }
}

