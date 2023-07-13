package db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import utils.Utils;

import db.Table;
import model.Film;
import model.Genre;
import model.Period;

public final class FilmsTable implements Table<Film, Integer> {    
    
    public static final String TABLE_NAME = "film";

    private final Connection connection; 

    public FilmsTable(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean createTable() {
        // 1. Create the statement from the open connection inside a try-with-resources
        try (final Statement statement = this.connection.createStatement()) {
            // 2. Execute the statement with the given query
            statement.executeUpdate(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "FilmID INTEGER NOT NULL AUTO_INCREMENT," +
                        "Titolo VARCHAR(100) NOT NULL," +
                        "Regista VARCHAR(40) NOT NULL," +
                        "Anno INTEGER NOT NULL," +
                        "Durata INTEGER NOT NULL," +
                        "Trama MEDIUMTEXT," +
                        "DataInizio DATETIME REFERENCES Period(DataInizio)," +
                        "DataFine DATETIME REFERENCES Period(DataFine)," +
                        "Genere VARCHAR(40)," +
                        "CONSTRAINT PK_Film PRIMARY KEY (FilmID) " +
                ")");
            return true;
        } catch (final SQLException e) {
            // 3. Handle possible SQLExceptions
            return false;
        }
    }

    @Override
    public boolean dropTable() {
        // 1. Create the statement from the open connection inside a try-with-resources
        try (final Statement statement = this.connection.createStatement()) {
            // 2. Execute the statement with the given query
            statement.executeUpdate("DROP TABLE " + TABLE_NAME );
            return true;
        } catch (final SQLException e) {
            // 3. Handle possible SQLExceptions
            return false;
        }
    }

    @Override
    public Optional<Film> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE FilmID = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readFilmsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Film> readFilmsFromResultSet(final ResultSet resultSet) {
        final List<Film> films = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("FilmID");
                final String title = resultSet.getString("Titolo");
                final String director = resultSet.getString("Regista");
                final int duration = resultSet.getInt("Durata");
                final int year = resultSet.getInt("Anno");
                final String plot = resultSet.getString("Trama");
                final Date dataInzio = Utils.sqlDateToDate(resultSet.getDate("dataInizio"));
                final Date dataFine = Utils.sqlDateToDate(resultSet.getDate("dataFine"));
                final String genere = resultSet.getString("Genere");
                // After retrieving all the data we create a film object
                final Film film = new Film(id,title,director,duration,year,plot,new Period(dataInzio, dataFine), new Genre(genere));
                films.add(film);
            }
        } catch (final SQLException e) {}
        return films;
    }

    @Override
    public List<Film> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readFilmsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Film film) {
        final String query = "INSERT INTO " + TABLE_NAME + "(FilmID, Titolo, Regista, Anno, Durata, Trama, DataInizio, DataFine, Genere) VALUES (?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, film.getId());
            statement.setString(2, film.getTitle());
            statement.setString(3, film.getDirector());
            statement.setInt(4, film.getYear());
            statement.setInt(5, film.getDuration());
            statement.setString(6, film.getPlot());
            statement.setDate(7, Utils.dateToSqlDate(film.getPeriod().getStartDate()));
            statement.setDate(8, Utils.dateToSqlDate(film.getPeriod().getEndDate()));
            statement.setString(9, film.getGenre().getType());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE FilmID = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Film film) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Titolo = ?," + 
                "Regista = ?," +
                "Anno = ?," + 
                "Durata = ?," + 
                "Trama = ?," +
                "DataInizio = ?," + 
                "DataFine = ?," + 
                "Genere= ?" +
            "WHERE FilmID = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, film.getTitle());
            statement.setString(2, film.getDirector());
            statement.setInt(3, film.getYear());
            statement.setInt(4, film.getDuration());
            statement.setString(5, film.getPlot());
            statement.setDate(6,Utils.dateToSqlDate(film.getPeriod().getStartDate()));
            statement.setDate(7,Utils.dateToSqlDate(film.getPeriod().getEndDate()));
            statement.setString(8,film.getGenre().getType());
            statement.setInt(9, film.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}