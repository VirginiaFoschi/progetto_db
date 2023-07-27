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

import app.Controller;
import db.Table;
import model.Showing;
import utils.Pair;
import utils.Utils;

public final class ShowingsTable implements Table<Showing, Pair<Date,String>> {    
    
    public static final String TABLE_NAME = "PROIEZIONE";

    private final Connection connection; 

    public ShowingsTable(final Connection connection) {
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
    public Optional<Showing> findByPrimaryKey(final Pair<Date,String> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE data = ? AND oraInizio = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setDate(1, Utils.dateToSqlDate(id.getX()));
            statement.setTime(2,Utils.timeToSqlTime(id.getY()));
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) Showing from the ResultSet
            return readShowingsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the Showings in it and collects them in a List
     * @param resultSet a ResultSet from which the Showing(s) will be extracted
     * @return a List of all the Showings in the ResultSet
     */
    private List<Showing> readShowingsFromResultSet(final ResultSet resultSet) {
        final List<Showing> showings = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final Date data = Utils.sqlDateToDate(resultSet.getDate("data"));
                final String time = Utils.sqlTimeToTime(resultSet.getTime("oraInizio"));
                final int salaID = resultSet.getInt("codiceSala");
                final int numSpettatori = resultSet.getInt("numeroSpettatori");
                final int filmID = resultSet.getInt("codiceFilm");
                final String tipo = resultSet.getString("tipo");
                // After retrieving all the data we create a Showing object
                final Showing showing = new Showing(data, time, numSpettatori, salaID, filmID, tipo);
                showings.add(showing);
            }
        } catch (final SQLException e) {}
        return showings;
    }

    @Override
    public List<Showing> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readShowingsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Showing showing) {
        final String query = "INSERT INTO " + TABLE_NAME + "(data,oraInizio,codiceSala,numeroSpettatori,codiceFilm,tipo) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(showing.getData()));
            statement.setTime(2,Utils.timeToSqlTime(showing.getStartTime()));
            statement.setInt(3,showing.getTheaterID());
            statement.setInt(4, showing.getNumberSpectator());
            statement.setInt(5, showing.getFilmID());
            statement.setString(6, showing.getProgrammingMode());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Date,String> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE data = ? AND oraInizio = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(id.getX()));
            statement.setTime(2,Utils.timeToSqlTime(id.getY()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Showing showing) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "codiceSala = ?," +
                "numeroSpettatori = ?," +
                "codiceFilm = ?," +
                "tipo = ? "+
            "WHERE data = ? AND oraInizio = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,showing.getTheaterID());
            statement.setInt(2,showing.getNumberSpectator());
            statement.setInt(3,showing.getFilmID());
            statement.setString(4,showing.getProgrammingMode());
            statement.setDate(5,Utils.dateToSqlDate(showing.getData()));
            statement.setTime(6,Utils.timeToSqlTime(showing.getStartTime()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public List<Showing> getFilmShows (final int filmID) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceFilm = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,filmID);
            final ResultSet resultSet = statement.executeQuery();
            return readShowingsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Showing> getFilmShowsOnDate (final int filmID, final Date dateShow) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceFilm = ? AND data = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,filmID);
            statement.setDate(2,Utils.dateToSqlDate(dateShow));
            final ResultSet resultSet = statement.executeQuery();
            return readShowingsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /*private boolean viewOrariFilm(final Date data, final int codiceSala) {
        final String query = "CREATE VIEW ORARI_FILM (codiceFilm,oraInizio,oraFine) "+
                            "AS ( SELECT P.codiceFilm, P.oraInizio, DATE_ADD(P.oraInizio , INTERVAL durata MINUTE) " +
                            "FROM " + TABLE_NAME + " P , " + Controller.getFilmsTable().getTableName() + " F " +
                            "WHERE P.codiceFilm = F.codiceFilm " + 
                            "AND P.data = ? AND P.codiceSala = ? ) "; 
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1,Utils.dateToSqlDate(data));
            statement.setInt(2,codiceSala);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isTheaterEmpty(Integer codiceSala, LocalDate data, String ora, int duration) {
        //if(viewOrariFilm(Utils.localDateToDate(data), duration)){
            final String query = "SELECT * FROM ORARI_FILM O , " + Controller.getFilmDetailTable().getTableName() + " F " +
                                "WHERE O.codiceFilm = F.codiceFilm " + 
                                "AND ((oraInizio <= ? AND oraFine >= ?) " +
                                "OR (oraInizio <= DATE_ADD(? , INTERVAL ? MINUTE) AND oraFine >= DATE_ADD(? , INTERVAL ? MINUTE)))";
            try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setTime(1,Utils.timeToSqlTime(ora));
                statement.setTime(2,Utils.timeToSqlTime(ora));
                statement.setTime(3,Utils.timeToSqlTime(ora));
                statement.setInt(4,duration);
                statement.setTime(5,Utils.timeToSqlTime(ora));
                statement.setInt(6,duration);
                final ResultSet resultSet = statement.executeQuery();
                return !resultSet.next();
            } catch (final SQLException e) {
                throw new IllegalStateException(e);
            }
    } */

    private boolean viewOrariFilm(final Date data, final int codiceSala) {
        final String query = "CREATE VIEW ORARI_FILM (codiceFilm,oraInizio,oraFine) "+
                            "AS ( SELECT P.codiceFilm, P.oraInizio, DATE_ADD(P.oraInizio , INTERVAL durata MINUTE) " +
                            "FROM " + TABLE_NAME + " P , " + Controller.getFilmsTable().getTableName() + " F " +
                            "WHERE P.codiceFilm = F.codiceFilm " + 
                            "AND P.data = ? AND P.codiceSala = ? ) "; 
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1,Utils.dateToSqlDate(data));
            statement.setInt(2,codiceSala);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isTheaterEmpty(Integer codiceSala, LocalDate data, String ora, int duration) {
        //if(viewOrariFilm(Utils.localDateToDate(data), duration)){
            final String query = "SELECT * FROM " + TABLE_NAME + " P, " + Controller.getFilmsTable().getTableName() + " F " +
                                "WHERE P.codiceFilm = F.codiceFilm " + 
                                "AND P.codiceSala = ? AND P.data = ? " +
                                "AND ((oraInizio <= ? AND DATE_ADD(P.oraInizio , INTERVAL F.durata MINUTE) >= ?) " +
                                "OR (oraInizio <= DATE_ADD(? , INTERVAL ? MINUTE) AND DATE_ADD(P.oraInizio , INTERVAL F.durata MINUTE) >= DATE_ADD(? , INTERVAL ? MINUTE))) ";
            try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1,codiceSala);
                statement.setDate(2,Utils.localDateToSQLDate(data));
                statement.setTime(3,Utils.timeToSqlTime(ora));
                statement.setTime(4,Utils.timeToSqlTime(ora));
                statement.setTime(5,Utils.timeToSqlTime(ora));
                statement.setInt(6,duration);
                statement.setTime(7,Utils.timeToSqlTime(ora));
                statement.setInt(8,duration);
                final ResultSet resultSet = statement.executeQuery();
                return !resultSet.next();
            } catch (final SQLException e) {
                throw new IllegalStateException(e);
            }
    }

}