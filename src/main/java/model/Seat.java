package model;

public class Seat {
    
    private final String line;
    private final int number;
    private final int theater;
    
    public Seat(String line, int number, int theater) {
        this.line = line;
        this.number = number;
        this.theater = theater;
    }

    public String getLine() {
        return line;
    }

    public int getNumber() {
        return number;
    }

    public int getTheater() {
        return theater;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        result = prime * result + number;
        result = prime * result + theater;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Seat other = (Seat) obj;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        if (number != other.number)
            return false;
        if (theater != other.theater)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Seat [line=" + line + ", number=" + number + ", theater=" + theater + "]";
    }
    
}