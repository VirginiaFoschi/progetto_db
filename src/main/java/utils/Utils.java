package utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

public final class Utils {
    private Utils() {}
    
    public static java.util.Date sqlDateToDate(final java.sql.Date sqlDate) {
        return sqlDate == null ? null : new java.util.Date(sqlDate.getTime());
    }
    
    public static java.sql.Date dateToSqlDate(final java.util.Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }
    
    public static Optional<java.util.Date> buildDate(final int day, final int month, final int year) {
        try {
            final String dateFormatString = "dd/MM/yyyy";
            final String dateString = day + "/" + month + "/" + year;
            final java.util.Date date = new SimpleDateFormat(dateFormatString, Locale.ITALIAN).parse(dateString);
            return Optional.of(date);
        } catch (final ParseException e) {
            return Optional.empty();
        }
    }

    public static java.util.Date localDateToDate(LocalDate date){
        return sqlDateToDate(java.sql.Date.valueOf(date));
    }

    public static java.sql.Date localDateToSQLDate (LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    public static Time timeToSqlTime(final String time) {
        return Time.valueOf(time.concat(":00"));
    }

    public static String sqlTimeToTime(final Time time) {
        String tempo = time.toString();
        return tempo.substring(0, tempo.length() - 3);
    }

    public static String buildTime (final int ore, final int minuti) {
        return ore + ":" + minuti;
    }
}
