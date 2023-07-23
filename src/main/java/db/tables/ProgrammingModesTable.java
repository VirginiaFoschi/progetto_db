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
import model.ProgrammingMode;

public final class ProgrammingModesTable implements Table<ProgrammingMode, String> {    
    
    public static final String TABLE_NAME = "MODALITA_PROGRAMMAZIONE";

    private final Connection connection; 

    public ProgrammingModesTable(final Connection connection) {
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
    public Optional<ProgrammingMode> findByPrimaryKey(final String id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE tipo = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readProgrammingModesFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<ProgrammingMode> readProgrammingModesFromResultSet(final ResultSet resultSet) {
        final List<ProgrammingMode> programmingModes = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String type = resultSet.getString("tipo");
                final String viewingExperience = resultSet.getString("esperienzaDiVisione");
                final String visualEffect = resultSet.getString("effettoVisivo");
                // After retrieving all the data we create a film object
                final ProgrammingMode programmingMode = new ProgrammingMode(type,viewingExperience,visualEffect);
                programmingModes.add(programmingMode);
            }
        } catch (final SQLException e) {}
        return programmingModes;
    }

    @Override
    public List<ProgrammingMode> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readProgrammingModesFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final ProgrammingMode programmingMode) {
        final String query = "INSERT INTO " + TABLE_NAME + "(tipo,esperienzaDiVisione,effettoVisivo) VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, programmingMode.getType());
            statement.setString(2, programmingMode.getViewingExperience());
            statement.setString(3, programmingMode.getVisualEffect());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final String id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final ProgrammingMode programmingMode) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "esperienzaDiVisione = ?," +
                "effettoVisivo = ?" +
            "WHERE tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,programmingMode.getViewingExperience());
            statement.setString(2,programmingMode.getVisualEffect());
            statement.setString(3,programmingMode.getType());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}