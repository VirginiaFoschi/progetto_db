package model;

import java.util.Objects;

public class Line {
    
    private final int theater;
    private final String letter;

    public Line(int theater, String letter) {
        this.theater = theater;
        this.letter = letter;
    }

    public int getTheater() {
        return theater;
    }

    public String getLetter() {
        return letter;
    }

    @Override
    public String toString() {
        return "Line [theater=" + theater + ", letter=" + letter + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(theater,letter);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Line)
                && ((Line) other).getTheater() == this.getTheater()
                && ((Line) other).getLetter() == this.getLetter();
    }
    

}