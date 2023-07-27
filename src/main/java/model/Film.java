package model;

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
    private final int duration;
    private final int year;
    private final Optional<String> plot;
    private final Period period;
    private final int codiceRegista;

    public Film(int id, String title, int duration, int year, Optional<String> plot, Period period, int codiceRegista) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.year = year;
        this.plot = plot;
        this.period = period;
        this.codiceRegista=codiceRegista;
    }

    public Film(String title, int duration, int year, Optional<String> plot, Period period,int codiceRegista) {
        this(0,title,duration,year,plot,period,codiceRegista);
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

    public Optional<String> getPlot() {
        return plot;
    }

    public Period getPeriod() {
        return period;
    }

    public int getCodiceRegista() {
        return codiceRegista;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + duration;
        result = prime * result + year;
        result = prime * result + ((plot == null) ? 0 : plot.hashCode());
        result = prime * result + ((period == null) ? 0 : period.hashCode());
        result = prime * result + codiceRegista;
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
        Film other = (Film) obj;
        if (id != other.id)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (duration != other.duration)
            return false;
        if (year != other.year)
            return false;
        if (plot == null) {
            if (other.plot != null)
                return false;
        } else if (!plot.equals(other.plot))
            return false;
        if (period == null) {
            if (other.period != null)
                return false;
        } else if (!period.equals(other.period))
            return false;
        if (codiceRegista != other.codiceRegista)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Film [id=" + id + ", title=" + title + ", duration=" + duration + ", year=" + year + ", plot=" + plot
                + ", period=" + period + ", codiceRegista=" + codiceRegista + "]";
    }
    
}
