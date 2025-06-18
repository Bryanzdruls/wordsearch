package com.demo.wordsearch.sockets;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.demo.wordsearch.model.Board;
import com.demo.wordsearch.model.Player;
import com.demo.wordsearch.model.Word;
import com.demo.wordsearch.service.GameService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/game/{username}")
@ApplicationScoped
public class GameSocket {

    Map<String, Player> players = new ConcurrentHashMap<>();
    private final GameService gameService = new GameService();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int TIME_LIMIT_MINUTES = 1; // Límite de tiempo en minutos

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        Board board = new Board(20,20);
        Player player = new Player(username, session, board);
        players.put(username, player);
        sendMessage(session, "Bienvenido, " + username + "! Tu tablero ha sido generado.");
        board.printBoard();
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        Player player = players.get(username);
        if (player == null) return;

        if (player.getExpectedWordCount() == -1) {
            int count = 0;
            try {
                count = Integer.parseInt(message.trim());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Debes ingresar un número válido de palabras.");
            }

            if (count < 5 || count > 15) {
                sendMessage(player.getSession(), "Por favor ingresa un número entre 5 y 15.");
            } else {
                player.setExpectedWordCount(count);
                sendMessage(player.getSession(), "Vas a ingresar " + count + " palabras. Escribe la palabra #1:");
            }
        } else if (player.getWords().size() < player.getExpectedWordCount()) {
            String wordText = message.trim().toUpperCase();

            if (wordText.length() > 10) {
                sendMessage(player.getSession(), "❌ La palabra es muy larga. Máximo 10 letras.");
                return;
            }

            Word newWord = new Word(wordText);
            boolean added = player.getWords().add(newWord);

            if (!added) {
                sendMessage(player.getSession(), "❌ Esa palabra ya fue ingresada.");
                return;
            }

            int current = player.getWords().size();
            int total = player.getExpectedWordCount();

            if (current < total) {
                sendMessage(player.getSession(), "✅ Palabra #" + current + " recibida. Escribe la palabra #" + (current + 1) + ":");
            } else {
                sendMessage(player.getSession(), "✅ Todas las palabras recibidas. Generando tablero...");
                startGameTimer(player); // Iniciar temporizador

                // Generar tablero
                gameService.generateBoard(player);
                String boardStr = gameService.getBoardAsString(player.getBoard());
                String wordsStr = String.join(",", player.getWords().stream()
                        .map(Word::getText)
                        .collect(Collectors.toList()));

                sendMessage(player.getSession(), "BOARD:" + boardStr);
                sendMessage(player.getSession(), "WORDS:" + wordsStr);
                sendMessage(player.getSession(), "TIME:" + TIME_LIMIT_MINUTES); // Enviar límite de tiempo
            }
        }
    }

    private void startGameTimer(Player player) {
        scheduler.schedule(() -> {
            if (players.containsKey(player.getName())) {
                Session session = player.getSession();
                try {
                    sendMessage(session, "TIME_UP");
                    sendMessage(session, "⌛ ¡Se acabó el tiempo! Juego terminado.");
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Tiempo agotado"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                players.remove(player.getName());
            }
        }, TIME_LIMIT_MINUTES, TimeUnit.MINUTES);
    }


    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        players.remove(username);
        System.out.println("Jugador " + username + " se desconectó.");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        players.remove(username);
        System.err.println("Error con el jugador " + username + ": " + throwable.getMessage());
        sendMessage(session,throwable.getMessage());
    }

    private void sendMessage(Session session, String message) {
        session.getAsyncRemote().sendText(message);
    }
}