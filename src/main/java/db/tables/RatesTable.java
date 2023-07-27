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
import model.Category;
import model.Rate;

public final class RatesTable implements Table<Rate, Pair<Category,String>> {    
    
    public static final String TABLE_NAME = "TARIFFARIO";

    private final Connection connection; 

    public RatesTable(final Connection connection) {
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
    public Optional<Rate> findByPrimaryKey(final Pair<Category,String> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE nomeCategoria = ?  AND tipo = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,id.getX().getName());
            statement.setString(2, id.getY());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readRatesFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Rate> readRatesFromResultSet(final ResultSet resultSet) {
        final List<Rate> rates = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String category = resultSet.getString("nomeCategoria");
                final String type = resultSet.getString("tipo");
                final double price = resultSet.getDouble("prezzo");
                // After retrieving all the data we create a film object
                final Rate rate = new Rate(category,price,type);
                rates.add(rate);
            }
        } catch (final SQLException e) {}
        return rates;
    }

    @Override
    public List<Rate> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readRatesFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Rate rate) {
        final String query = "INSERT INTO " + TABLE_NAME + "(nomeCategoria,tipo,prezzo) VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, rate.getCategoria());
            statement.setString(2, rate.getTipo());
            statement.setDouble(3, rate.getPrezzo());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Category,String> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE nomeCategoria = ?  AND tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getX().getName());
            statement.setString(2, id.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Rate rate) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "prezzo = ? " +
            "WHERE nomeCategoria = ? AND tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDouble(1,rate.getPrezzo());
            statement.setString(2,rate.getCategoria());
            statement.setString(3,rate.getTipo());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}