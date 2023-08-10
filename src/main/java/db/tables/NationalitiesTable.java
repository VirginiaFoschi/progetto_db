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
import model.Nationality;

public final class NationalitiesTable implements Table<Nationality, String> {    
    
    public static final String TABLE_NAME = "NAZIONALITA";

    private final Connection connection; 

    public NationalitiesTable(final Connection connection) {
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
    public Optional<Nationality> findByPrimaryKey(final String name) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE nomeNazionalità = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, name);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) Nationality from the ResultSet
            return readNationalitiesFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the Nationalitys in it and collects them in a List
     * @param resultSet a ResultSet from which the Nationality(s) will be extracted
     * @return a List of all the Nationalitys in the ResultSet
     */
    private List<Nationality> readNationalitiesFromResultSet(final ResultSet resultSet) {
        final List<Nationality> nationalities = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String name = resultSet.getString("nomeNazionalità");
                // After retrieving all the data we create a Nationality object
                final Nationality nationality = new Nationality(name);
                nationalities.add(nationality);
            }
        } catch (final SQLException e) {}
        return nationalities;
    }

    @Override
    public List<Nationality> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readNationalitiesFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Nationality Nationality) {
        final String query = "INSERT INTO " + TABLE_NAME + "(nomeNazionalità) VALUES (?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, Nationality.getName());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final String name) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE nomeNazionalità = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Nationality Nationality) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "nomeNazionalità = ?" +
            "WHERE nomeNazionalità = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,Nationality.getName());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}