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

public final class SeatsTable implements Table<Seat, Pair<String,Integer>> {    
    
    public static final String TABLE_NAME = "Seat";

    private final Connection connection; 

    public SeatsTable(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean createTable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTable'");
    }

    @Override
    public boolean dropTable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dropTable'");
    }

    @Override
    public Optional<Seat> findByPrimaryKey(Pair<String, Integer> primaryKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByPrimaryKey'");
    }

    @Override
    public List<Seat> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public boolean save(Seat value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public boolean update(Seat updatedValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Pair<String, Integer> primaryKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    

}