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
import model.Cast;

public final class CastsTable implements Table<Cast, Integer> {    
    
    public static final String TABLE_NAME = "CAST";

    private final Connection connection; 

    public CastsTable(final Connection connection) {
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
    public Optional<Cast> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codice = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readCastsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Cast> readCastsFromResultSet(final ResultSet resultSet) {
        final List<Cast> casts = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("codice");
                final String nome = resultSet.getString("nome");
                final String cognome = resultSet.getString("cognome");
                final String nazionalita = resultSet.getString("nazionalita");
                final Boolean regista = resultSet.getBoolean("regista");
                // After retrieving all the data we create a film object
                final Cast Cast = new Cast(id,nome,cognome,nazionalita,regista);
                casts.add(Cast);
            }
        } catch (final SQLException e) {}
        return casts;
    }

    @Override
    public List<Cast> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCastsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Cast cast) {
        final String query = "INSERT INTO " + TABLE_NAME + "(codice,nome,cognome,nazionalita,regista) VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, cast.getId());
            statement.setString(2, cast.getNome());
            statement.setString(3, cast.getCognome());
            statement.setString(4, cast.getNazionalita());
            statement.setBoolean(5, cast.isRegista());
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
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codice = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Cast cast) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "nome = ?," + 
                "cognome = ?," +
                "nazionalita = ?," +
                "regista = ? " +
            "WHERE codice = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,cast.getNome());
            statement.setString(2,cast.getCognome());
            statement.setString(3,cast.getNazionalita());
            statement.setBoolean(4, cast.isRegista());
            statement.setInt(5,cast.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public boolean isActor(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codice = ? AND regista = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            statement.setBoolean(2, false);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return resultSet.getRow()!=0 ? true : false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Cast> getDirectors() {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE regista = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setBoolean(1, true);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readCastsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}