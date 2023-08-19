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

import app.Controller;
import utils.Pair;
import utils.Utils;

import db.Table;
import model.CineCard;
import model.Client;

public final class CinecardsTable implements Table<CineCard, Pair<Client,Date>> {    
    
    public static final String TABLE_NAME = "CINECARD";

    private final Connection connection; 

    public CinecardsTable(final Connection connection) {
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
    public Optional<CineCard> findByPrimaryKey(final Pair<Client,Date> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CF = ?  AND dataAcquisto = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,id.getX().getCf());
            statement.setDate(2,Utils.dateToSqlDate(id.getY()));
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readCineCardsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<CineCard> readCineCardsFromResultSet(final ResultSet resultSet) {
        final List<CineCard> cineCards = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String cf = resultSet.getString("CF");
                final Date dataAcquisto = Utils.sqlDateToDateWithTime(resultSet.getTimestamp("dataAcquisto"));
                final int ingressiDisponibili = resultSet.getInt("numeroIngressiDisponibili");
                final int numeroIngressi = resultSet.getInt("numeroIngressiTotali");
                // After retrieving all the data we create a film object
                final CineCard cineCard = new CineCard(cf,dataAcquisto,ingressiDisponibili,numeroIngressi);
                cineCards.add(cineCard);
            }
        } catch (final SQLException e) {}
        return cineCards;
    }

    @Override
    public List<CineCard> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCineCardsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final CineCard cineCard) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CF,dataAcquisto,numeroIngressiDisponibili,numeroIngressiTotali) VALUES (?,NOW(),?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, cineCard.getClient_cf());
            statement.setInt(2, cineCard.getIngressiDisponibili());
            statement.setInt(3, cineCard.getIngressiTotali());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Client,Date> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CF = ?  AND dataAcquisto = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getX().getCf());
            statement.setDate(2, Utils.dateToSqlDate(id.getY()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final CineCard cineCard) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "numeroIngressiDisponibili = ?," +
                "numeroIngressiTotali = ? " +
            "WHERE CF = ? AND dataAcquisto = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1,cineCard.getIngressiDisponibili());
            statement.setInt(2,cineCard.getIngressiTotali());
            statement.setString(3,cineCard.getClient_cf());
            statement.setDate(4,Utils.dateToSqlDate(cineCard.getDataAcquisto()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public Optional<CineCard> getNewer(final String client) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CF = ?  ORDER BY dataAcquisto DESC LIMIT 1";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,client);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            List<CineCard> list = readCineCardsFromResultSet(resultSet);
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<CineCard> getCinecardFromClient(final String cf) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CF = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,cf);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            List<CineCard> all = readCineCardsFromResultSet(resultSet);
            Optional<CineCard> card = hasCinecardValid(cf);
            return all.stream().map(x->card.isPresent() && x.equals(card.get()) ? x : new CineCard(x.getClient_cf(),x.getDataAcquisto(),0,x.getIngressiTotali(),false)).toList();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<CineCard> hasCinecardValid(final String cf) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * "+
                            "FROM " + TABLE_NAME + " C, " + Controller.getCinecardTypeTable().getTableName() + " CT "+
                            "WHERE C.numeroIngressiTotali = CT.numeroIngressiTotali "+
                            "AND CF = ? AND numeroIngressiDisponibili > 0 "+
                            "AND DATE_ADD(dataAcquisto,INTERVAL CT.mesiValidit\u00E0 MONTH) >= NOW() ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1,cf);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readCineCardsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}