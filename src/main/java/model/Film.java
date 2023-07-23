package model;

import java.util.Objects;

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
    private final int duration;
    private final int year;
    private final String plot;
    private final Period period;
    
    public Film(int id, String title, int duration, int year, String plot, Period period) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.year = year;
        this.plot = plot;
        this.period = period;
    }

    public Film(String title, int duration, int year, String plot, Period period) {
        this(0,title,duration,year,plot,period);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    @Override
    public boolean equals(final Object other) {
        return (other instanceof Film)
                && ((Film) other).getId() == this.getId()
                && ((Film) other).getTitle().equals(this.getTitle())
                && ((Film) other).getDuration() == this.getDuration()
                && ((Film) other).getYear() == this.getYear()
                && ((Film) other).getPlot().equals(this.getPlot())
                && ((Film) other).getPeriod().equals(this.getPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,title,duration,year,plot,period);
    }
}
