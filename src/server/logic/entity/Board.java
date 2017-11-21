package server.logic.entity;

import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable{
    private int height;
    private int width;
    private Slot[][] matrix;


    public Board() {
        this.height = 6;
        this.width = 7;
        this.matrix = new Slot[width][height];
        for (int i = 0; i < matrix.length; i++) {
            Arrays.fill(matrix[i], Slot.EMPTY);
        }
    }

    public void putPiece(int row, Slot slot) {
        int column = 0;
        row = row-1;
        for (int i = 0; i <= height-1; i++) {
            if(matrix[row][i].equals(Slot.EMPTY)) {
                matrix[row][i] = slot;
                column = i;
                if (checkWinCondition(row, column, slot)) {
                    System.out.println("Winner winner chicken dinner");
                }
                break;
            }
        }
    }

    public void forcePiece(int row, int column) {
        matrix[row-1][column-1] = Slot.PLAYER2;
    }

    private boolean checkWinCondition(int row, int column, Slot slot) {
        int connected = 1;
        //check vertical
        connected += connectedInDirection(slot, row, column, 0, -1);
        if (connected == 4)
            return true;
        connected = 1;
        //check horizontal
        connected += connectedInDirection(slot, row, column, -1, 0);
        connected += connectedInDirection(slot, row, column, 1, 0);
        if (connected >= 4)
            return true;
        connected = 1;
        //check diagonally right
        connected += connectedInDirection(slot, row, column, 1, -1);
        connected += connectedInDirection(slot, row, column, -1, 1);
        if (connected >= 4)
            return true;
        connected = 1;
        //check diagonally left
        connected += connectedInDirection(slot, row, column, 1, 1);
        connected += connectedInDirection(slot, row, column, -1, -1);
        if (connected >= 4)
            return true;
        return false;
    }

    private int connectedInDirection(Slot slot, int row, int column, int x, int y) {
        int connected = 0;
        while(isWithinGame(row+x, column+y)) {
            if (matrix[row+x][column+y].equals(slot)) {
                connected++;
                column+=y;
                row+=x;
            } else {
                break;
            }
        }
        return connected;
    }
    private boolean isWithinGame(int row, int column) {
        return (row >= 0 && row < width && column >= 0 && column < height);
    }

    @Override
    public String toString(){
        String output = "";
        for (int i = height-1; i >= 0; i--) {
            for (int j = 0; j < matrix.length; j++) {
                switch (String.valueOf(matrix[j][i])) {
                    case "EMPTY":
                        output += "[ ]";
                        break;
                    case "PLAYER1":
                        output += "[X]";
                        break;
                    case "PLAYER2":
                        output += "[O]";
                        break;
                }
            }
            output += "\n";
        }
        //generate player options
        for (int i = 1; i<=width; i++) {
            output += " "+i+" ";
        }
        return output;
    }
}

