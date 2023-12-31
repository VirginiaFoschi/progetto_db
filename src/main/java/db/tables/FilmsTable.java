package db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import app.Controller;
import utils.Utils;

import db.Table;
import model.Film;
import model.Period;

public final class FilmsTable implements Table<Film, Integer> {    
    
    public static final String TABLE_NAME = "FILM";

    private final Connection connection; 

    public FilmsTable(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
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
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceFilm = ?";
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
                final int id = resultSet.getInt("codiceFilm");
                final String title = resultSet.getString("titolo");
                final int duration = resultSet.getInt("durata");
                final int year = resultSet.getInt("anno");
                final int registerID = resultSet.getInt("codiceRegista");
                final Optional<String> plot = Optional.ofNullable(resultSet.getString("trama"));
                final Date dataInzio = Utils.sqlDateToDate(resultSet.getDate("dataInizio"));
                final Date dataFine = Utils.sqlDateToDate(resultSet.getDate("dataFine"));
                // After retrieving all the data we create a film object
                final Film film = new Film(id,title,duration,year,plot,new Period(dataInzio, dataFine),registerID);
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

    public Integer getLastID() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT codiceFilm FROM " + TABLE_NAME + " WHERE codiceFilm = LAST_INSERT_ID() ");
            resultSet.next();
            int id = resultSet.getInt("codiceFilm");
            return id;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Film film) {
        final String query = "INSERT INTO " + TABLE_NAME + " (titolo,durata,anno,codiceRegista,trama,dataInizio,dataFine) VALUES (?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getDuration());
            statement.setInt(3, film.getYear());
            statement.setInt(4,film.getCodiceRegista());
            statement.setString(5, film.getPlot().orElse(null));
            statement.setDate(6, Utils.dateToSqlDate(film.getPeriod().getStartDate()));
            statement.setDate(7, Utils.dateToSqlDate(film.getPeriod().getEndDate()));
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
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codiceFilm = ?";
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
                "titolo = ?," + 
                "durata = ?," + 
                "anno = ?," + 
                "codiceRegista = ?," +
                "trama = ?," +
                "dataInizio = ?," + 
                "dataFine = ? " + 
            "WHERE codiceFilm = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getDuration());
            statement.setInt(3, film.getYear());
            statement.setInt(4,film.getCodiceRegista());
            statement.setString(5, film.getPlot().orElse(null));
            statement.setDate(6,Utils.dateToSqlDate(film.getPeriod().getStartDate()));
            statement.setDate(7,Utils.dateToSqlDate(film.getPeriod().getEndDate()));
            statement.setInt(8, film.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }


    public List<Film> fromIDtoFilm (final List<Integer> id) {
        return id.stream()
                .map(x->findByPrimaryKey(x).orElse(null))
                .filter(x->x!=null)
                .collect(Collectors.toList());
    }
    

    public Boolean checkPeriod(final Integer id, final LocalDate date) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceFilm = ? AND DATEDIFF(?,dataInizio) >= 0 AND DATEDIFF(dataFine,?) >= 0 ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            statement.setDate(2,Utils.localDateToSQLDate(date));
            statement.setDate(3,Utils.localDateToSQLDate(date));
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return resultSet.next();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Film> getFilmsOnDate(final Date date) {
        final String query = "SELECT DISTINCT F.* "+
                            "FROM " + TABLE_NAME + " F, " + Controller.getShowingTable().getTableName() + " S " +
                            "WHERE F.codiceFilm = S.codiceFilm AND data = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1,Utils.dateToSqlDate(date));
            final ResultSet resultSet = statement.executeQuery();
            return readFilmsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Film> getFilmsFromGenre(final String genre) {
        final String query = "SELECT F.* "+
                            "FROM " + TABLE_NAME + " F, " + Controller.getCorrispondenceTable().getTableName() + " C " +
                            "WHERE F.codiceFilm = C.codiceFilm AND C.tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,genre);
            final ResultSet resultSet = statement.executeQuery();
            return readFilmsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}