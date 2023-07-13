package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/***
 * Class representing a Film with simple fields: id, title, director, DurationgetDuration.
 * The corresponding database table is:
 *    CREATE TABLE Films (
 *       id INT NOT NULL PRIMARY KEY,
 *       title CHAR(40) NOT NULL,
 *       director CHAR (40) NOT NULL,
 *       DurationgetDuration DATE
 *    )
 */

// The Film class could be defined in a way more concise way with the use of a record class.
// It is a fairly new and very useful new Java feature:
// https://docs.oracle.com/en/java/javase/17/language/records.html
public class Film {
    
    private final int id;
    private final String title;
    private final String director;
    private final int duration;
    private final int year;
    private final String plot;
    private final Period period;
    private final Genre genre;
    
    public Film(int id, String title, String director, int duration, int year, String plot, Period period,
            Genre genre) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.duration = duration;
        this.year = year;
        this.plot = plot;
        this.period = period;
        this.genre = genre;
    }

    /*public Date getStartDate(){
        return period.getStartDate();
    }

    public Date getEndDate(){
        return period.getEndDate();
    } */

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getDuration() {
        return duration;
    }

    public int getYear() {
        return year;
    }

    public String getPlot() {
        return plot;
    }

    public Period getPeriod() {
        return period;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "Film [id=" + id + ", title=" + title + ", director=" + director + ", duration=" + duration + ", year="
                + year + ", plot=" + plot + ", period=" + period + ", genre=" + genre + "]";
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof Film)
                && ((Film) other).getId() == this.getId()
                && ((Film) other).getTitle().equals(this.getTitle())
                && ((Film) other).getDirector().equals(this.getDirector())
                && ((Film) other).getDuration() == this.getDuration()
                && ((Film) other).getYear() == this.getYear()
                && ((Film) other).getPlot().equals(this.getPlot())
                && ((Film) other).getPeriod().equals(this.getPeriod())
                && ((Film) other).getGenre().equals(this.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,title,director,duration,year,plot,period,genre);
    }
}
