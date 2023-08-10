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
import model.Client;
import utils.Utils;

public final class ClientsTable implements Table<Client, String> {    
    
    public static final String TABLE_NAME = "CLIENTE";

    private final Connection connection; 

    public ClientsTable(final Connection connection) {
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
    public Optional<Client> findByPrimaryKey(final String id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CF = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            return readClientsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a ResultSet read all the films in it and collects them in a List
     * @param resultSet a ResultSet from which the film(s) will be extracted
     * @return a List of all the films in the ResultSet
     */
    private List<Client> readClientsFromResultSet(final ResultSet resultSet) {
        final List<Client> clients = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final String id = resultSet.getString("CF");
                final String nome = resultSet.getString("nome");
                final String cognome = resultSet.getString("cognome");
                final Date dataNascita = Utils.sqlDateToDate(resultSet.getDate("dataNascita"));
                final Optional<String> telefono = Optional.ofNullable(resultSet.getString("telefono"));
                final String mail = resultSet.getString("mail");
                // After retrieving all the data we create a film object
                final Client client = new Client(id,nome,cognome,dataNascita,telefono,mail);
                clients.add(client);
            }
        } catch (final SQLException e) {}
        return clients;
    }

    @Override
    public List<Client> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readClientsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Client client) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CF,nome,cognome,dataNascita,telefono,mail) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, client.getCf());
            statement.setString(2, client.getNome());
            statement.setString(3, client.getCognome());
            statement.setDate(4,Utils.dateToSqlDate(client.getDataNascita()));
            statement.setString(5, client.getTelefono().orElse(null));
            statement.setString(6, client.getMail());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final String id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CF = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Client client) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "nome = ?," + 
                "cognome = ?," +
                "dataNascita = ?," +
                "telefono = ?," +
                "mail = ? " +
            "WHERE CF = ? ";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1,client.getNome());
            statement.setString(2,client.getCognome());
            statement.setDate(3, Utils.dateToSqlDate(client.getDataNascita()));
            statement.setString(4, client.getTelefono().orElse(null));
            statement.setString(5,client.getMail());
            statement.setString(6,client.getCf());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            System.out.println(e);
            throw new IllegalStateException(e);
        }
    }

    public Integer getYears(final String id, final Date data) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT CF, TIMESTAMPDIFF(YEAR,dataNascita,?) AS ETA FROM " + TABLE_NAME + " WHERE CF = ? ";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setDate(1, Utils.dateToSqlDate(data));
            statement.setString(2, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution; 
            //    here we extract the first (and only) film from the ResultSet
            resultSet.next();
            return resultSet.getInt("eta");
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}