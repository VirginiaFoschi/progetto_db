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
import model.Category;

public final class CategoriesTable implements Table<Category, String> {    
    
    public static final String TABLE_NAME = "CATEGORIA";

    private final Connection connection; 

    public CategoriesTable(final Connection connection) {
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
    public Optional<Category> findByPrimaryKey(final String name) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE nomeCategoria = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, name);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) Category from the ResultSet
            return readCategorysFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the Categorys in it and collects them in a List
     * @param resultSet a ResultSet from which the Category(s) will be extracted
     * @return a List of all the Categorys in the ResultSet
     */
    private List<Category> readCategorysFromResultSet(final ResultSet resultSet) {
        final List<Category> categories = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String name = resultSet.getString("nomeCategoria");
                // After retrieving all the data we create a Category object
                final Category category = new Category(name);
                categories.add(category);
            }
        } catch (final SQLException e) {}
        return categories;
    }

    @Override
    public List<Category> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCategorysFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Category category) {
        final String query = "INSERT INTO " + TABLE_NAME + "(nomeCategoria) VALUES (?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
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
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE nomeCategoria = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Category category) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "nomeCategoria = ?" +
            "WHERE nomeCategoria = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,category.getName());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

}