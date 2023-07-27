package db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import db.Table;
import model.Film;
import model.FilmDetail;
import utils.Pair;

public final class FilmDetailsTable implements Table<FilmDetail, Pair<String,Film>> {    
    
    public static final String TABLE_NAME = "DETTAGLIO_FILM";

    private final Connection connection; 

    public FilmDetailsTable(final Connection connection) {
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
    public Optional<FilmDetail> findByPrimaryKey(final Pair<String,Film> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE tipo = ? AND codiceFilm = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, id.getX());
            statement.setInt(2,id.getY().getId());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) FilmDetail from the ResultSet
            return readFilmDetailsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the FilmDetails in it and collects them in a List
     * @param resultSet a ResultSet from which the FilmDetail(s) will be extracted
     * @return a List of all the FilmDetails in the ResultSet
     */
    private List<FilmDetail> readFilmDetailsFromResultSet(final ResultSet resultSet) {
        final List<FilmDetail> filmDetails = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String filmType = resultSet.getString("tipo");
                final int filmID = resultSet.getInt("codiceFilm");
                // After retrieving all the data we create a FilmDetail object
                final FilmDetail filmDetail = new FilmDetail(filmType,filmID);
                filmDetails.add(filmDetail);
            }
        } catch (final SQLException e) {}
        return filmDetails;
    }

    @Override
    public List<FilmDetail> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readFilmDetailsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final FilmDetail filmDetail) {
        final String query = "INSERT INTO " + TABLE_NAME + "(tipo,codiceFilm) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, filmDetail.getFilmType());
            statement.setInt(2,filmDetail.getFilmID());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<String,Film> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE tipo = ? AND codiceFilm = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getX());
            statement.setInt(2,id.getY().getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final FilmDetail filmDetail) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "tipo = ?," +
                "codiceFilm = ? " +
            "WHERE tipo = ? AND codiceFilm = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,filmDetail.getFilmType());
            statement.setInt(2,filmDetail.getFilmID());
            statement.setString(3,filmDetail.getFilmType());
            statement.setInt(4,filmDetail.getFilmID());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public List<Integer> getFilmsInAllModes (final int programmingModes) {
        List<Integer> filmsID = new ArrayList<>();
        final String query = "SELECT codiceFilm FROM " + TABLE_NAME + " GROUP BY codiceFilm HAVING COUNT(tipo) = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,programmingModes);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                filmsID.add(resultSet.getInt("codiceFilm"));
            }
            return filmsID;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}