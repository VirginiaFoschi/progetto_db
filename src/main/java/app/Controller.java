package app;

import java.io.IOException;

import db.ConnectionProvider;
import db.tables.CastsTable;
import db.tables.CorrispondencesTable;
import db.tables.FilmDetailsTable;
import db.tables.FilmsTable;
import db.tables.GenresTable;
import db.tables.LinesTable;
import db.tables.ParticipationsTable;
import db.tables.ProgrammingModesTable;
import db.tables.SeatsTable;
import db.tables.TheatersTable;
import model.Cast;
import model.ProgrammingMode;

public class Controller {

    final static String username = "root";
    final static String password = "";
    final static String dbName = "cinema";
    final static int NUM_SALE = 6;
    
    private static App app;
    private static ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    private static FilmsTable filmsTable = new FilmsTable(connectionProvider.getMySQLConnection());
    private static GenresTable genreTable = new GenresTable(connectionProvider.getMySQLConnection());
    private static TheatersTable theatreTable = new TheatersTable(connectionProvider.getMySQLConnection());
    private static SeatsTable seatTable = new SeatsTable(connectionProvider.getMySQLConnection());
    private static LinesTable lineTable = new LinesTable(connectionProvider.getMySQLConnection());
    private static CastsTable castTable = new CastsTable(connectionProvider.getMySQLConnection());
    private static CorrispondencesTable corrispondenceTable = new CorrispondencesTable(connectionProvider.getMySQLConnection());
    private static ParticipationsTable participationTable = new ParticipationsTable(connectionProvider.getMySQLConnection());
    private static FilmDetailsTable filmDetailTable = new FilmDetailsTable(connectionProvider.getMySQLConnection());
    private static ProgrammingModesTable programmingModesTable = new ProgrammingModesTable(connectionProvider.getMySQLConnection());

    public Controller(final App application) {
        this.app = application;
    }

    public static ProgrammingModesTable getProgrammingModesTable() {
        return programmingModesTable;
    }

    public static FilmDetailsTable getFilmDetailTable() {
        return filmDetailTable;
    }

    public static ParticipationsTable getParticipationTable() {
        return participationTable;
    }

    public static CorrispondencesTable getCorrispondenceTable() {
        return corrispondenceTable;
    }

    public static CastsTable getCastTable() {
        return castTable;
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

    public static void insertFilm() throws IOException {
        app.insertFilm();
    }

    public static void view() throws IOException {
        app.view();
    }

}