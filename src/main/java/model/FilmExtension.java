package model;

import java.util.List;

public class FilmExtension extends Film {

    private final String genres;

    public FilmExtension(int id, String title, int duration, int year, String plot, Period period,List<String> genres) {
        super(id,title, duration, year, plot, period);
        this.genres=genres.toString();
    }

    public String getGenres() {
        return genres;
    }
    
}