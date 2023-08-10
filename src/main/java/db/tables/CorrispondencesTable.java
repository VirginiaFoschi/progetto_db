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

import utils.Pair;

import db.Table;
import model.Corrispondence;
import model.Film;

public final class CorrispondencesTable implements Table<Corrispondence, Pair<Film,String>> {    
    
    public static final String TABLE_NAME = "CORRISPONDENZA";

    private final Connection connection; 

    public CorrispondencesTable(final Connection connection) {
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
    public Optional<Corrispondence> findByPrimaryKey(final Pair<Film,String> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceFilm = ?  AND tipo = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,id.getX().getId());
            statement.setString(2, id.getY());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readCorrispondencesFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Corrispondence> readCorrispondencesFromResultSet(final ResultSet resultSet) {
        final List<Corrispondence> corrispondences = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String genre = resultSet.getString("tipo");
                final int filmID = resultSet.getInt("codiceFilm");
                // After retrieving all the data we create a film object
                final Corrispondence corrispondence = new Corrispondence(filmID,genre);
                corrispondences.add(corrispondence);
            }
        } catch (final SQLException e) {}
        return corrispondences;
    }

    @Override
    public List<Corrispondence> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCorrispondencesFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Corrispondence corrispondence) {
        final String query = "INSERT INTO " + TABLE_NAME + "(tipo,codiceFilm) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, corrispondence.getGenre());
            statement.setInt(2, corrispondence.getFilmID());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Film,String> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE tipo = ?  AND codiceFilm = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getY());
            statement.setInt(2, id.getX().getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean deleteFilm(final Film id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codiceFilm = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Corrispondence corrispondence) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "tipo = ?," + 
                "codiceFilm = ? " +
            "WHERE tipo = ? AND codiceFilm = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,corrispondence.getGenre());
            statement.setInt(2,corrispondence.getFilmID());
            statement.setString(3,corrispondence.getGenre());
            statement.setInt(4,corrispondence.getFilmID());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public List<String> getFilmGenre(final Integer filmID) {
        List<String> genres = new ArrayList<>();
        final String query = "SELECT tipo FROM " + TABLE_NAME + " WHERE codiceFilm = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,filmID);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            while (resultSet.next()) {
                genres.add(resultSet.getString("tipo"));
            }
            return genres;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}