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

import utils.Pair;
import utils.Utils;

import db.Table;
import model.Period;

public final class PeriodsTable implements Table<Period, Pair<Date,Date>> {    
    
    public static final String TABLE_NAME = "PERIODO";

    private final Connection connection; 

    public PeriodsTable(final Connection connection) {
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
    public Optional<Period> findByPrimaryKey(final Pair<Date,Date> data) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE dataInizio = ? AND dataFine = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setDate(1, Utils.dateToSqlDate(data.getX()));
            statement.setDate(2, Utils.dateToSqlDate(data.getY()));
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readPeriodsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Period> readPeriodsFromResultSet(final ResultSet resultSet) {
        final List<Period> periods = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final Date dataInzio = Utils.sqlDateToDate(resultSet.getDate("dataInizio"));
                final Date dataFine = Utils.sqlDateToDate(resultSet.getDate("dataFine"));
                // After retrieving all the data we create a film object
                final Period period = new Period(dataInzio, dataFine);
                periods.add(period);
            }
        } catch (final SQLException e) {}
        return periods;
    }

    @Override
    public List<Period> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readPeriodsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Period period) {
        final String query = "INSERT INTO " + TABLE_NAME + "(dataInizio, dataFine) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(period.getStartDate()));
            statement.setDate(2, Utils.dateToSqlDate(period.getEndDate()));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Date,Date> data) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE dataInizio = ? AND dataFine = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(data.getX()));
            statement.setDate(2, Utils.dateToSqlDate(data.getY()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Period period) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "dataInizio = ?," + 
                "dataFine = ?" + 
            "WHERE dataInizio = ? AND dataFine = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1,Utils.dateToSqlDate(period.getStartDate()));
            statement.setDate(2,Utils.dateToSqlDate(period.getEndDate()));
            statement.setDate(3,Utils.dateToSqlDate(period.getStartDate()));
            statement.setDate(4,Utils.dateToSqlDate(period.getEndDate()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}