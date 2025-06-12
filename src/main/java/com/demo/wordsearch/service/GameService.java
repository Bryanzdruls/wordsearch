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
        int rows = board.getGrid().length;               // número de filas
        int cols = board.getGrid()[0].length;            // número de columnas (asumiendo todas las filas son iguales)
        Set<Word> words = player.getWords();

        for (Word word : words) {
            boolean inserted = false;
            int attempts = 0;
            while (!inserted && attempts < 100) {
                int row = random.nextInt(rows);
                int col = random.nextInt(cols - word.getText().length());
                inserted = board.insertWordHorizontally(word.getText(), row, col);
                if (inserted) {
                    word.setCoordinates(row, col, row, col + word.getText().length() - 1);
                }
                attempts++;
            }
            if (!inserted) {
                System.out.println("❌ No se pudo insertar la palabra: " + word.getText());
            }
        }

        board.fillRemainingWithRandomLetters();
        player.setBoard(board);
    }

    public String getBoardAsString(Board board) {
        board.printBoard();
        StringBuilder sb = new StringBuilder("🧩 Tu tablero:\n");
        for (char[] row : board.getGrid()) {
            for (char c : row) {
                sb.append(c).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
