package com.example.a2048game;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static List<Pair<Directions,int[][]>> getPossibleMoves(int[][] board) {
        List<Pair<Directions,int[][]>> listOfPossibleMoves = new ArrayList<>();
        Pair<Boolean,int[][]> afterMove;
        for (Directions direction : Directions.values()) {
            afterMove=move(direction,board);
            if (afterMove.first) {
                listOfPossibleMoves.add(new Pair<>(direction,afterMove.second));
            }
        }
        return listOfPossibleMoves;
    }

    public static Pair<Boolean,int[][]> move(Directions dir,int[][] board){
        Pair<Boolean,int[][]> afterMove;
        if(dir==Directions.UP){
            afterMove=Move.moveUp(board);
        }else if(dir==Directions.DOWN){
            afterMove=Move.moveDown(board);
        }else if(dir==Directions.RIGHT){
            afterMove=Move.moveRight(board);
        }else {
            afterMove=Move.moveLeft(board);
        }
        return afterMove;
    }

    public static ArrayList<int[]> getEmptyCells(int[][] board){
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for(int r =0 ; r<4;r++){
            for(int c =0 ; c<4;c++){
                if(board[r][c] == 0){
                    emptyCells.add(new int[]{r,c});
                }
            }
        }
        return emptyCells;
    }

    public static boolean isGameOver(int[][] boardValues){
        // Check if any tile has a value of 2048.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (boardValues[i][j] == 2048) {
                    Log.d("Result", "YOU WIN!");
                    return false;
                }
            }
        }
        // Check if there are any empty tiles on the board.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (boardValues[i][j] == 0) {
                    return false;
                }
            }
        }
        // Check if there are any adjacent tiles with the same value.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int currentValue = boardValues[i][j];

                // Check the tile to the right.
                if (j < 4 - 1 && boardValues[i][j + 1] == currentValue) {
                    return false;
                }

                // Check the tile below.
                if (i < 4 - 1 && boardValues[i + 1][j] == currentValue) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int evaluateBoard(int[][] board) {
        int score = 0;
        int maxTile = 0;

        // Calculate the total score and find the maximum tile value
        for (int[] row : board) {
            for (int value : row) {
                score += value;
                if (value > maxTile) {
                    maxTile = value;
                }
            }
        }
        // Add bonus score based on the maximum tile value
        if (maxTile == 2048) {
            score += 10000; // Bonus for reaching the 2048 tile
        } else {
            score += Math.log(maxTile) / Math.log(2); // Logarithmic bonus for higher tiles
        }

        return score;
    }
    public static int[][] addRandomNum(int[][] board){
        ArrayList<int[]> emptySpaces=getEmptyCells(board);
        if(isGameOver(board)||emptySpaces.isEmpty())
            return board;
        Random random = new Random();
        int randomNo = random.nextInt(emptySpaces.size());
        int[] randomPosition = emptySpaces.get(randomNo);
        int row = randomPosition[0];
        int column = randomPosition[1];
        emptySpaces.remove(randomNo);
        int addedRandomNum = random.nextInt(2);
        if(addedRandomNum == 0){
            board[row][column] = 4;
        }else{
            board[row][column] = 2;
        }
        return board;
    }

}
