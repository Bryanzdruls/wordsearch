package com.demo.wordsearch.model;


public class Word {
    private final String text;
    private boolean found;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;

    public Word(String text) {
        this.text = text.toUpperCase();
        this.found = false;
    }

    public String getText() {
        return text;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public void setCoordinates(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public String getPosition() {
        return "(" + startRow + "," + startCol + ") to (" + endRow + "," + endCol + ")";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Word word = (Word) obj;
        return text.equals(word.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

}
