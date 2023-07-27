package model;

import java.util.List;
import java.util.Optional;

public class FilmExtension extends Film {

    private final String genres;

    public FilmExtension(int id, String title, int duration, int year, Optional<String> plot, Period period,int codiceRegista,List<String> genres) {
        super(id,title, duration, year, plot, period,codiceRegista);
        this.genres=genres.toString();
    }

    public String getGenres() {
        return genres;
    }
    
}