package com.demo.wordsearch.model;

import com.demo.wordsearch.model.enums.GameState;
import jakarta.websocket.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    private final String name;
    private final Session session;
    private Board board;
    private Set<Word> words = new HashSet<>();
    private GameState state = GameState.WAITING_FOR_WORDS;

    private int expectedWordCount = -1;
    private final long startTime;
    private long endTime;

    public Player(String name, Session session, Board board) {
        this.name = name;
        this.session = session;
        this.board = board;
        this.startTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }

    public Board getBoard() {
        return board;
    }

    public void endGame() {
        this.endTime = System.currentTimeMillis();
    }

    public long getTimeSpentMillis() {
        return endTime - startTime;
    }

    public void setExpectedWordCount(int count) {
        this.expectedWordCount = count;
    }

    public int getExpectedWordCount() {
        return expectedWordCount;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setBoard(Board board) {
        this.board=board;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}