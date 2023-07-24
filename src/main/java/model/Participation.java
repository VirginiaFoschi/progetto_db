package model;

public class Participation {
    
    private final int filmID;
    private final int castID;
    
    public Participation(int filmID, int castID) {
        this.filmID = filmID;
        this.castID = castID;
    }

    public int getFilmID() {
        return filmID;
    }

    public int getCastID() {
        return castID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + filmID;
        result = prime * result + castID;
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
        Participation other = (Participation) obj;
        if (filmID != other.filmID)
            return false;
        if (castID != other.castID)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Participation [filmID=" + filmID + ", castID=" + castID + "]";
    }
    
}