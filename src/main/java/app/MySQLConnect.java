package app;

import java.sql.Connection;

import controller.FilmController;
import db.ConnectionProvider;
import db.tables.FilmsTable;

public class MySQLConnect {

    final static String username = "root";
    final static String password = "";
    final static String dbName = "cinema";
    
    private static ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    private static FilmsTable filmsTable = new FilmsTable(connectionProvider.getMySQLConnection());

    public MySQLConnect(){
        setUp();
    }

    private void setUp(){
        filmsTable.dropTable();
        filmsTable.createTable();
    }

    public static FilmsTable getFilmsTable(){
        return filmsTable;
    }

}