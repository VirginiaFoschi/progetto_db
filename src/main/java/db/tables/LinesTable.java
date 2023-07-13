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
import model.Line;

public final class LinesTable implements Table<Line, Pair<String,Integer>> {    
    
    public static final String TABLE_NAME = "Line";

    private final Connection connection; 

    public LinesTable(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean createTable() {
        // 1. Create the statement from the open connection inside a try-with-resources
        try (final Statement statement = this.connection.createStatement()) {
            // 2. Execute the statement with the given query
            statement.executeUpdate(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "Letter CHAR(1) AUTO_INCREMENT," +
                        "TheaterID INTEGER REFERENCES Theater(theaterID)," +
                        "CONSTRAINT PK_Line PRIMARY KEY (Letter,TheaterID) " +
                ")");
            return true;
        } catch (final SQLException e) {
            // 3. Handle possible SQLExceptions
            return false;
        }
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
    public Optional<Line> findByPrimaryKey(final Pair<String,Integer> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Letter = ?  AND TheaterID = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,id.getX());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readLinesFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Line> readLinesFromResultSet(final ResultSet resultSet) {
        final List<Line> Lines = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String letter = resultSet.getString("Letter");
                final int theaterID = resultSet.getInt("TheaterID");
                // After retrieving all the data we create a film object
                final Line Line = new Line(theaterID,letter);
                Lines.add(Line);
            }
        } catch (final SQLException e) {}
        return Lines;
    }

    @Override
    public List<Line> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readLinesFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Line Line) {
        final String query = "INSERT INTO " + TABLE_NAME + "(Letter,TheaterID) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, Line.getLetter());
            statement.setInt(2, Line.getTheater());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<String,Integer> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Letter = ?  AND TheaterID = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getX());
            statement.setInt(2, id.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Line Line) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Letter = ?," + 
                "TheaterID = ? " +
            "WHERE Letter = ? AND Theater = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,Line.getLetter());
            statement.setInt(2,Line.getTheater());
            statement.setString(3,Line.getLetter());
            statement.setInt(4,Line.getTheater());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}