package com.example.a2048game;

import android.util.Pair;

public class Move {
    public static int score =0;
    public static Pair<Boolean,int[][]> moveUp(int[][] board){
        Pair<Boolean,int[][]> afterMoveUp = moveLeft(transpose(board));
        int[][] afterTranspose=transpose(afterMoveUp.second);
        return new Pair<>(afterMoveUp.first,afterTranspose);
    }
    public static Pair<Boolean,int[][]> moveDown(int[][] board){
        Pair<Boolean,int[][]> afterMoveRight = moveRight(transpose(board));
        int[][] boardAfterTranspose=transpose(afterMoveRight.second);
        return new Pair<>(afterMoveRight.first,boardAfterTranspose);
    }
    public static Pair<Boolean,int[][]> moveRight(int[][] board){
        Pair<Boolean,int[][]> afterMoveLeft = moveLeft(reverse(board));
        int[][] boardAfterReverse=reverse(afterMoveLeft.second);
        return new Pair<>(afterMoveLeft.first,boardAfterReverse);
    }
    public static Pair<Boolean,int[][]> moveLeft(int[][] board){
        Pair<Boolean,int[][]> afterCompress = compress(board);
        Pair<Boolean,int[][]> afterMerge = merge(afterCompress.second);
        Pair<Boolean,int[][]>  afterSecCompress = compress(afterMerge.second);
        return new Pair<>(afterCompress.first || afterMerge.first || afterSecCompress.first,afterSecCompress.second);
    }


    private static int[][] reverse(int[][] board) {
        int[][] newBoardValues = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newBoardValues[i][j] = board[i][3 - j];
            }
        }
        return newBoardValues;
    }
    private static int[][] transpose(int[][] board) {
        int[][] newBoardValues = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newBoardValues[i][j] = board[j][i];
            }
        }
        return newBoardValues;
    }
    private static Pair<Boolean,int[][]>  merge(int[][] board) {
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == board[i][j + 1] && board[i][j] != 0) {
                    board[i][j] = board[i][j] * 2;
                    score+=board[i][j] * 2;
                    board[i][j + 1] = 0;
                    changed = true;
                }
            }
        }
        return new Pair<>(changed,board);
    }
    private static Pair<Boolean,int[][]> compress(int[][] board) {
        boolean changed = false;
        int[][] newBoardValues = new int[4][4];
        for (int i = 0; i < 4; i++) {
            int pos = 0;
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    newBoardValues[i][pos] = board[i][j];
                    if (j != pos) {
                        changed = true;
                    }
                    pos++;
                }
            }
        }
        return new Pair<>(changed,newBoardValues);
    }

}
