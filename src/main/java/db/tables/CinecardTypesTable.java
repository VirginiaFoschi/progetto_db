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
import model.CinecardType;

public final class CinecardTypesTable implements Table<CinecardType, Integer> {    
    
    public static final String TABLE_NAME = "TIPOLOGIA_CINECARD";

    private final Connection connection; 

    public CinecardTypesTable(final Connection connection) {
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
    public Optional<CinecardType> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE numeroIngressiTotali = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readCinecardTypesFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<CinecardType> readCinecardTypesFromResultSet(final ResultSet resultSet) {
        final List<CinecardType> cinecardTypes = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int entrancesNumber = resultSet.getInt("numeroIngressiTotali");
                final Double price = resultSet.getDouble("prezzo");
                final int validityMonths = resultSet.getInt("mesiValidit\u00E0");
                // After retrieving all the data we create a film object
                final CinecardType cinecardType = new CinecardType(entrancesNumber,price,validityMonths);
                cinecardTypes.add(cinecardType);
            }
        } catch (final SQLException e) {}
        return cinecardTypes;
    }

    @Override
    public List<CinecardType> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCinecardTypesFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final CinecardType cinecardType) {
        final String query = "INSERT INTO " + TABLE_NAME + "(numeroIngressiTotali,prezzo,mesiValidit\u00E0) VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,cinecardType.getEntrancesNumber());
            statement.setDouble(2, cinecardType.getPrice());
            statement.setInt(3,cinecardType.getValidityMonths());
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
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE numeroIngressiTotali = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final CinecardType cinecardType) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "prezzo = ?," +
                "mesiValidit\u00E0 = ? " +
            "WHERE numeroIngressiTotali = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDouble(1,cinecardType.getPrice());
            statement.setInt(2,cinecardType.getValidityMonths());
            statement.setInt(3,cinecardType.getEntrancesNumber());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}