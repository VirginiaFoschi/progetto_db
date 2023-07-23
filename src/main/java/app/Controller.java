package app;

import db.ConnectionProvider;
import db.tables.FilmsTable;
import db.tables.GenresTable;
import db.tables.LinesTable;
import db.tables.SeatsTable;
import db.tables.TheatersTable;

public class Controller {

    final static String username = "root";
    final static String password = "";
    final static String dbName = "cinema";
    final static int NUM_SALE = 6;
    
    private static ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    private static FilmsTable filmsTable = new FilmsTable(connectionProvider.getMySQLConnection());
    private static GenresTable genreTable = new GenresTable(connectionProvider.getMySQLConnection());
    private static TheatersTable theatreTable = new TheatersTable(connectionProvider.getMySQLConnection());
    private static SeatsTable seatTable = new SeatsTable(connectionProvider.getMySQLConnection());
    private static LinesTable lineTable = new LinesTable(connectionProvider.getMySQLConnection());

    public Controller(){
        
    }

    public static GenresTable getGenreTable() {
        return genreTable;
    }

    public static TheatersTable getTheatreTable() {
        return theatreTable;
    }

    public static SeatsTable getSeatTable() {
        return seatTable;
    }

    public static LinesTable getLineTable() {
        return lineTable;
    }

    public static FilmsTable getFilmsTable(){
        return filmsTable;
    }

}