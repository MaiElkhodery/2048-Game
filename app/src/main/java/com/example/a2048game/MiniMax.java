package com.example.a2048game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.util.Pair;

public class MiniMax {
    private static final int MAX_DEPTH = 5;
    private static final int BOARD_SIZE=4;


    // This method returns the best move using the Minimax algorithm
    public static Pair<Directions, int[][]> getBestMove(int[][] board) {
        List<Pair<Directions, int[][]>> listOfPossibleMoves = getPossibleMoves(board);

        int bestScore = Integer.MIN_VALUE;
        Pair<Directions, int[][]> bestMove = null;

        for (Pair<Directions, int[][]> move : listOfPossibleMoves) {
            int score = minimax(move.second, MAX_DEPTH, true);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    // This method recursively evaluates the game tree using the Minimax algorithm
    private static int minimax(int[][] board, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || isGameOver(board)) {
            return calculateScore(board);
        }
        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            List<Pair<Directions, int[][]>> listOfPossibleMoves = getPossibleMoves(board);

            for (Pair<Directions, int[][]> move : listOfPossibleMoves) {
                int score = minimax(move.second, depth - 1, true);
                bestScore = Math.max(bestScore, score);
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            ArrayList<int[]> emptyCells = getEmptyCells(board);

            for (int[] cell : emptyCells) {
                int[][] newBoard = Arrays.copyOf(board, board.length);
                newBoard[cell[0]][cell[1]] = 2;

                int score = minimax(newBoard, depth - 1, true);
                bestScore = Math.min(bestScore, score);

                newBoard = Arrays.copyOf(board, board.length);
                newBoard[cell[0]][cell[1]] = 4;

                score = minimax(newBoard, depth - 1, true);
                bestScore = Math.min(bestScore, score);
            }

            return bestScore;
        }
    }
    // This method returns the score for the given board
    private static int calculateScore(int[][] board) {
        int score = 0;
        for (int[] row : board) {
            for (int value : row) {
                score += value;
            }
        }
        return score;
    }
    private static boolean isGameOver(int[][] boardValues){
        // Check if any tile has a value of 2048.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardValues[i][j] == 2048) {
                    Log.d("Result", "YOU WIN!");
                    return false;
                }
            }
        }
        // Check if there are any empty tiles on the board.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardValues[i][j] == 0) {
                    return false;
                }
            }
        }
        // Check if there are any adjacent tiles with the same value.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int currentValue = boardValues[i][j];

                // Check the tile to the right.
                if (j < BOARD_SIZE - 1 && boardValues[i][j + 1] == currentValue) {
                    return false;
                }

                // Check the tile below.
                if (i < BOARD_SIZE - 1 && boardValues[i + 1][j] == currentValue) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Pair<Directions,int[][]>> getPossibleMoves(int[][] board) {
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

    private static Pair<Boolean,int[][]> move(Directions dir,int[][] board){
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

    private static ArrayList<int[]> getEmptyCells(int[][] board){
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for(int r =0 ; r<BOARD_SIZE;r++){
            for(int c =0 ; c<BOARD_SIZE;c++){
                if(board[r][c] == 0){
                    emptyCells.add(new int[]{r,c});
                }
            }
        }
        return emptyCells;
    }

}
