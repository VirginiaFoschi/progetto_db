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
import utils.Pair;

import db.Table;
import model.Actor;
import model.Cast;
import model.Film;
import model.Participation;

public final class ParticipationsTable implements Table<Participation, Pair<Film,Cast>> {    
    
    public static final String TABLE_NAME = "PARTECIPAZIONE";

    private final Connection connection; 

    public ParticipationsTable(final Connection connection) {
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
    public Optional<Participation> findByPrimaryKey(final Pair<Film,Cast> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceFilm = ?  AND codiceAttore = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,id.getX().getId());
            statement.setInt(2,id.getY().getId());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readParticipationsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Participation> readParticipationsFromResultSet(final ResultSet resultSet) {
        final List<Participation> participations = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int actorID = resultSet.getInt("codiceAttore");
                final int filmID = resultSet.getInt("codiceFilm");
                // After retrieving all the data we create a film object
                final Participation participation = new Participation(filmID,actorID);
                participations.add(participation);
            }
        } catch (final SQLException e) {}
        return participations;
    }

    @Override
    public List<Participation> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readParticipationsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Participation participation) {
        final String query = "INSERT INTO " + TABLE_NAME + "(codiceFilm,codiceAttore) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, participation.getFilmID());
            statement.setInt(2, participation.getCastID());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Film,Cast> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codiceFilm = ?  AND codiceAttore = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getX().getId());
            statement.setInt(2, id.getY().getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Participation participation) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "codiceFilm = ?," + 
                "codiceAttore = ? " +
            "WHERE codiceFilm = ? AND codiceAttore = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,participation.getFilmID());
            statement.setInt(2,participation.getCastID());
            statement.setInt(3,participation.getFilmID());
            statement.setInt(4,participation.getCastID());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public List<Integer> getFilmsFromActor(final Integer actorID) {
        List<Integer> films = new ArrayList<>();
        final String query = "SELECT codiceFilm FROM " + TABLE_NAME + " WHERE codiceAttore = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,actorID);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            while (resultSet.next()) {
                films.add(resultSet.getInt("codiceFilm"));
            }
            return films;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Integer> getActorFromFilm(final Integer filmID) {
        List<Integer> actors = new ArrayList<>();
        final String query = "SELECT codiceAttore FROM " + TABLE_NAME + " WHERE codiceFilm = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1,filmID);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            while (resultSet.next()) {
                actors.add(resultSet.getInt("codiceAttore"));
            }
            return actors;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Cast> getActorInOrder() {
        List<Cast> actors = new ArrayList<>();
        final String query = "SELECT A.* " +
                            "FROM " + TABLE_NAME + " P , " + Controller.getActorTable().getTableName() + " A " +
                            "WHERE P.codiceAttore = A.codiceAttore " +
                            "GROUP BY A.codiceAttore, nome, cognome, nazionalità ORDER BY COUNT(*) DESC ";
        // 2. Prepare a statement inside a try-with-resources
         try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final int id = resultSet.getInt("codiceAttore");
                final String nome = resultSet.getString("nome");
                final String cognome = resultSet.getString("cognome");
                final String nazionalita = resultSet.getString("nazionalità");
                Cast actor = new Actor(id, nome, cognome, nazionalita);
                actors.add(actor);
            }
            return actors;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}