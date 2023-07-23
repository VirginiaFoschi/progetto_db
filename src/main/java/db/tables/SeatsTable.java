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
import model.Seat;

public final class SeatsTable implements Table<Seat, Pair<Integer,Pair<Integer,String>>> {    
    
    public static final String TABLE_NAME = "POSTO";

    private final Connection connection; 

    public SeatsTable(final Connection connection) {
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
    public Optional<Seat> findByPrimaryKey(final Pair<Integer,Pair<Integer,String>> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE lettera = ?  AND codice = ? AND numero = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,id.getY().getY());
            statement.setInt(2, id.getY().getX());
            statement.setInt(3, id.getX());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readSeatsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Seat> readSeatsFromResultSet(final ResultSet resultSet) {
        final List<Seat> seats = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String letter = resultSet.getString("lettera");
                final int theaterID = resultSet.getInt("codice");
                final int number = resultSet.getInt("numero");
                // After retrieving all the data we create a film object
                final Seat seat = new Seat(letter,number,theaterID);
                seats.add(seat);
            }
        } catch (final SQLException e) {}
        return seats;
    }

    @Override
    public List<Seat> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readSeatsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Seat seat) {
        final String query = "INSERT INTO " + TABLE_NAME + "(lettera,codice,numero) VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, seat.getLine());
            statement.setInt(2, seat.getTheater());
            statement.setInt(3, seat.getNumber());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Integer,Pair<Integer,String>> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE lettera = ?  AND codice = ? AND numero = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getY().getY());
            statement.setInt(2, id.getY().getX());
            statement.setInt(3, id.getX());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Seat seat) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "lettera = ?," + 
                "codice = ?," +
                "numero = ? " +
            "WHERE lettera = ? AND codice = ? AND numero = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,seat.getLine());
            statement.setInt(2,seat.getTheater());
            statement.setInt(3,seat.getNumber());
            statement.setString(4,seat.getLine());
            statement.setInt(5,seat.getTheater());
            statement.setInt(6,seat.getNumber());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}