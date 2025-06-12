package com.demo.wordsearch.model;

import java.util.Arrays;
import java.util.Random;

public class Board {
    private final char[][] grid;
    private final int rows;
    private final int cols;
    private final Random random =new Random();

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
    }

    private void fillWithRandomLetters() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = (char) ('A' + random.nextInt(26));
            }
        }
    }
    public char[][] getGrid() {
        return grid;
    }

    public void setLetter(int row, int col, char letter) {
        grid[row][col] = letter;
    }

    public char getLetter(int row, int col) {
        return grid[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void printBoard() {
        for (char[] row : grid) {
            System.out.println(Arrays.toString(row));
        }
    }

    public boolean insertWordHorizontally(String word, int row, int colStart) {
        if (colStart + word.length() > cols) return false;

        // Validar si hay espacio sin choques
        for (int i = 0; i < word.length(); i++) {
            char current = grid[row][colStart + i];
            if (current != '\0' && current != word.charAt(i)) {
                return false; // Hay una letra distinta que no permite sobreescribir
            }
        }

        // Insertar palabra
        for (int i = 0; i < word.length(); i++) {
            grid[row][colStart + i] = word.charAt(i);
        }
        return true;
    }

    public void fillRemainingWithRandomLetters() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '\0') {
                    grid[i][j] = (char) ('A' + random.nextInt(26));
                }
            }
        }
    }

}
