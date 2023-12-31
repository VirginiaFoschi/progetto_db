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

import app.Controller;
import db.Table;
import model.Cast;
import model.Actor;

public final class ActorsTable implements Table<Cast, Integer> {    
    
    public static final String TABLE_NAME = "ATTORE";

    private final Connection connection; 

    public ActorsTable(final Connection connection) {
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
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceAttore = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readActorsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Cast> readActorsFromResultSet(final ResultSet resultSet) {
        final List<Cast> actors = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("codiceAttore");
                final String nome = resultSet.getString("nome");
                final String cognome = resultSet.getString("cognome");
                final Optional<String> nazionalita = Optional.ofNullable(resultSet.getString("nazionalit\u00E0"));
                // After retrieving all the data we create a film object
                final Cast actor = new Actor(id,nome,cognome,nazionalita);
                actors.add(actor);
            }
        } catch (final SQLException e) {}
        return actors;
    }

    @Override
    public List<Cast> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readActorsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Cast cast) {
        final String query = "INSERT INTO " + TABLE_NAME + "(codiceAttore,nome,cognome,nazionalit\u00E0) VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, cast.getId());
            statement.setString(2, cast.getNome());
            statement.setString(3, cast.getCognome());
            statement.setString(4, cast.getNazionalita().orElse(null));
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
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codiceAttore = ? ";
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
                "nazionalit\u00E0 = ?" +
            "WHERE codiceAttore = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,cast.getNome());
            statement.setString(2,cast.getCognome());
            statement.setString(3,cast.getNazionalita().orElse(null));
            statement.setInt(4,cast.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public List<Cast> getActorFromFilm(final Integer filmID) {
        final String query = "SELECT A.* "+
                            "FROM " + TABLE_NAME + " A, " + Controller.getParticipationTable().getTableName() + " P "+
                            "WHERE A.codiceAttore = P.codiceAttore AND codiceFilm = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,filmID);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readActorsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Integer getLastID() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT MAX(codiceAttore) AS last FROM " + TABLE_NAME );
            if(resultSet.next()) {
                return resultSet.getInt("last");
            } else {
                return 0;
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Cast> getStrangerActor(final int directorID) {
        final String query = "SELECT A.* "+
                            "FROM " + TABLE_NAME + " A " + 
                            "WHERE NOT EXISTS ( SELECT A1.* " +
                                            "FROM " + TABLE_NAME + " A1, " + Controller.getParticipationTable().getTableName() + " P, " + Controller.getFilmsTable().getTableName() + " F "+
                                            "WHERE A1.codiceAttore = P.codiceAttore " +
                                            "AND P.codiceFilm = F.codiceFilm " +
                                            "AND A1.codiceAttore = A.codiceAttore " +
                                            "AND F.codiceRegista = ? )";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,directorID);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readActorsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }      
    }

}