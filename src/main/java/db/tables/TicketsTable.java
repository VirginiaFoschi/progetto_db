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

import db.Table;
import javafx.util.Callback;
import model.Seat;
import model.Showing;
import model.Ticket;
import utils.Pair;
import utils.Utils;

public final class TicketsTable implements Table<Ticket,Pair<Seat,Showing>> {    
    
    public static final String TABLE_NAME = "BIGLIETTO";

    private final Connection connection; 

    public TicketsTable(final Connection connection) {
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
    public Optional<Ticket> findByPrimaryKey(final Pair<Seat,Showing> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codiceSala = ? AND letteraFila = ? AND numeroPosto = ? AND dataProiezione = ? AND oraInizio = ? AND codiceFilm = ? AND tipo = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id.getX().getTheater());
            statement.setString(2, id.getX().getLine());
            statement.setInt(3,id.getX().getNumber());
            statement.setDate(4,Utils.dateToSqlDate(id.getY().getData()));
            statement.setTime(5,Utils.timeToSqlTime(id.getY().getStartTime()));
            statement.setInt(6, id.getY().getFilmID());
            statement.setString(7, id.getY().getProgrammingMode());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) Ticket from the ResultSet
            return readTicketsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the Tickets in it and collects them in a List
     * @param resultSet a ResultSet from which the Ticket(s) will be extracted
     * @return a List of all the Tickets in the ResultSet
     */
    private List<Ticket> readTicketsFromResultSet(final ResultSet resultSet) {
        final List<Ticket> tickets = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final Integer salaID = resultSet.getInt("codiceSala");
                final String letterLine = resultSet.getString("letteraFila");
                final Integer numberSeat = resultSet.getInt("numeroPosto");
                final Date dateShow = Utils.sqlDateToDate(resultSet.getDate("dataProiezione"));
                final String startTime = Utils.sqlTimeToTime(resultSet.getTime("oraInizio"));
                final Date purchaseDate = Utils.sqlDateToDate(resultSet.getDate("dataAcquisto"));
                final boolean cineCard = resultSet.getBoolean("cineCard");
                final String clientID = resultSet.getString("CFcliente");
                final int filmId = resultSet.getInt("codiceFilm");
                final String typeFilm = resultSet.getString("tipo");
                // After retrieving all the data we create a Ticket object
                final Ticket ticket = new Ticket(dateShow, startTime, salaID, letterLine, numberSeat, purchaseDate, cineCard, clientID,filmId,typeFilm);
                tickets.add(ticket);
            }
        } catch (final SQLException e) {}
        return tickets;
    }

    @Override
    public List<Ticket> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readTicketsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Ticket ticket) {
        final String query = "INSERT INTO " + TABLE_NAME + "(dataProiezione,oraInizio,codiceSala,letteraFila,numeroPosto,dataAcquisto,cineCard,CFcliente,codiceFilm,tipo) VALUES (?,?,?,?,?,CURRENT_DATE(),?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1,Utils.dateToSqlDate(ticket.getDateShow()));
            statement.setTime(2,Utils.timeToSqlTime(ticket.getStartTime()));
            statement.setInt(3,ticket.getSalaID());
            statement.setString(4,ticket.getLetterLine());
            statement.setInt(5,ticket.getNumberSeat());
            statement.setBoolean(6, ticket.isCineCard());
            statement.setString(7,ticket.getClientID());
            statement.setInt(8, ticket.getFilmID());
            statement.setString(9, ticket.getTypeFilm());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Seat,Showing> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codiceSala = ? AND letteraFila = ? AND numeroPosto = ? AND dataProiezione = ? AND oraInizio = ? AND codiceFilm = ? AND tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getX().getTheater());
            statement.setString(2, id.getX().getLine());
            statement.setInt(3,id.getX().getNumber());
            statement.setDate(4,Utils.dateToSqlDate(id.getY().getData()));
            statement.setTime(5,Utils.timeToSqlTime(id.getY().getStartTime()));
            statement.setInt(6, id.getY().getFilmID());
            statement.setString(7, id.getY().getProgrammingMode());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Ticket ticket) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "dataAcquisto = ?," +
                "cineCard = ?," +
                "CFcliente = ? "+
            "WHERE codiceSala = ? AND letteraFila = ? AND numeroPosto = ? AND dataProiezione = ? AND oraInizio = ? AND codiceFilm = ? AND tipo = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1,Utils.dateToSqlDate(ticket.getPurchaseDate()));
            statement.setBoolean(2, ticket.isCineCard());
            statement.setString(3,ticket.getClientID());
            statement.setInt(4, ticket.getSalaID());
            statement.setString(5, ticket.getLetterLine());
            statement.setInt(6,ticket.getNumberSeat());
            statement.setDate(7,Utils.dateToSqlDate(ticket.getDateShow()));
            statement.setTime(8,Utils.timeToSqlTime(ticket.getStartTime()));
            statement.setInt(9, ticket.getFilmID());
            statement.setString(10, ticket.getTypeFilm());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public List<Ticket> getClientTickets(final String clientCF) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CFcliente = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, clientCF);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) Ticket from the ResultSet
            return readTicketsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}