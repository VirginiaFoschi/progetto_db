package model;

public class Participation {
    
    private final int filmID;
    private final int actorID;
    
    public Participation(int filmID, int actorID) {
        this.filmID = filmID;
        this.actorID = actorID;
    }

    public int getFilmID() {
        return filmID;
    }

    public int getActorID() {
        return actorID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + filmID;
        result = prime * result + actorID;
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
        if (actorID != other.actorID)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Participation [filmID=" + filmID + ", actorID=" + actorID + "]";
    }

}