package com.demo.wordsearch.service;

import com.demo.wordsearch.model.Board;
import com.demo.wordsearch.model.Player;
import com.demo.wordsearch.model.Word;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameService {
    private final Random random = new Random();

    public void generateBoard(Player player) {
        Board board = player.getBoard();
        Random random = new Random();

        for (Word word : player.getWords()) {
            String wordText = word.getText();
            boolean wordPlaced = false;
            int attempts = 0;
            int maxAttempts = 100;

            while (!wordPlaced && attempts < maxAttempts) {
                attempts++;

                boolean horizontal = random.nextBoolean();

                if (horizontal) {
                    int row = random.nextInt(board.getRows());
                    int col = random.nextInt(board.getCols() - wordText.length() + 1);
                    wordPlaced = board.insertWordHorizontally(wordText, row, col);
                } else {
                    int row = random.nextInt(board.getRows() - wordText.length() + 1);
                    int col = random.nextInt(board.getCols());
                    wordPlaced = board.insertWordVertically(wordText, row, col);
                }
            }

            if (!wordPlaced) {
                // Si no se pudo colocar en ninguna dirección, forzar en horizontal
                int row = random.nextInt(board.getRows());
                int col = random.nextInt(board.getCols() - wordText.length() + 1);
                board.insertWordHorizontally(wordText, row, col);
            }
        }

        board.fillRemainingWithRandomLetters();
    }

    public String getBoardAsString(Board board) {
        board.printBoard();
        StringBuilder sb = new StringBuilder();
        for (char[] row : board.getGrid()) {
            for (char c : row) {
                sb.append(c).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
