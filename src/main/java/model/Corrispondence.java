package model;

public class Corrispondence {

    private final int filmID;
    private final String genre;

    public Corrispondence(int filmID, String genre) {
        this.filmID = filmID;
        this.genre = genre;
    }

    public int getFilmID() {
        return filmID;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + filmID;
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
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
        Corrispondence other = (Corrispondence) obj;
        if (filmID != other.filmID)
            return false;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Corrispondence [filmID=" + filmID + ", genre=" + genre + "]";
    }
    
}