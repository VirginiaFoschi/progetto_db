package model;

public class FilmDetail {

    private final String filmType;
    private final int filmID;

    public FilmDetail(String filmType, int filmID) {
        this.filmType = filmType;
        this.filmID = filmID;
    }

    public String getFilmType() {
        return filmType;
    }

    public int getFilmID() {
        return filmID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filmType == null) ? 0 : filmType.hashCode());
        result = prime * result + filmID;
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
        FilmDetail other = (FilmDetail) obj;
        if (filmType == null) {
            if (other.filmType != null)
                return false;
        } else if (!filmType.equals(other.filmType))
            return false;
        if (filmID != other.filmID)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FilmDetail [filmType=" + filmType + ", filmID=" + filmID + "]";
    }

}