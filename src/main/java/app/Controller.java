package app;

import java.io.IOException;

import db.ConnectionProvider;
import db.tables.ActorsTable;
import db.tables.AgeRangesTable;
import db.tables.CategoriesTable;
import db.tables.CinecardTypesTable;
import db.tables.CinecardsTable;
import db.tables.ClientsTable;
import db.tables.CorrispondencesTable;
import db.tables.DirectorsTable;
import db.tables.FilmDetailsTable;
import db.tables.FilmsTable;
import db.tables.GenresTable;
import db.tables.LinesTable;
import db.tables.NationalitiesTable;
import db.tables.ParticipationsTable;
import db.tables.PeriodsTable;
import db.tables.ProgrammingModesTable;
import db.tables.RatesTable;
import db.tables.SeatsTable;
import db.tables.ShowingsTable;
import db.tables.TheatersTable;
import db.tables.TicketsTable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Controller {

    final static String username = "root";
    final static String password = "";
    final static String dbName = "cinema";
    
    private static JavaFXApp app;
    private static ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    private static FilmsTable filmsTable = new FilmsTable(connectionProvider.getMySQLConnection());
    private static GenresTable genreTable = new GenresTable(connectionProvider.getMySQLConnection());
    private static TheatersTable theatreTable = new TheatersTable(connectionProvider.getMySQLConnection());
    private static SeatsTable seatTable = new SeatsTable(connectionProvider.getMySQLConnection());
    private static LinesTable lineTable = new LinesTable(connectionProvider.getMySQLConnection());
    private static ActorsTable actorTable = new ActorsTable(connectionProvider.getMySQLConnection());
    private static DirectorsTable directorTable = new DirectorsTable(connectionProvider.getMySQLConnection());
    private static AgeRangesTable ageRangeTable = new AgeRangesTable(connectionProvider.getMySQLConnection());
    private static CategoriesTable categoryTable = new CategoriesTable(connectionProvider.getMySQLConnection());
    private static CinecardsTable cinecardTable = new CinecardsTable(connectionProvider.getMySQLConnection());
    private static CinecardTypesTable cinecardTypeTable = new CinecardTypesTable(connectionProvider.getMySQLConnection());
    private static PeriodsTable periodTable = new PeriodsTable(connectionProvider.getMySQLConnection());
    private static RatesTable rateTable = new RatesTable(connectionProvider.getMySQLConnection());
    private static ShowingsTable showingTable = new ShowingsTable(connectionProvider.getMySQLConnection());
    private static TicketsTable ticketTable = new TicketsTable(connectionProvider.getMySQLConnection());
    private static CorrispondencesTable corrispondenceTable = new CorrispondencesTable(connectionProvider.getMySQLConnection());
    private static ParticipationsTable participationTable = new ParticipationsTable(connectionProvider.getMySQLConnection());
    private static FilmDetailsTable filmDetailTable = new FilmDetailsTable(connectionProvider.getMySQLConnection());
    private static ProgrammingModesTable programmingModesTable = new ProgrammingModesTable(connectionProvider.getMySQLConnection());
    private static ClientsTable clientTable = new ClientsTable(connectionProvider.getMySQLConnection());
    private static NationalitiesTable nationalityTable = new NationalitiesTable(connectionProvider.getMySQLConnection());

    public Controller(final JavaFXApp application) {
        app = application;
    }

    public static NationalitiesTable getNationalityTable() {
        return nationalityTable;
    }

    public static ClientsTable getClientTable() {
        return clientTable;
    }

    public static ActorsTable getActorTable() {
        return actorTable;
    }

    public static DirectorsTable getDirectorTable() {
        return directorTable;
    }

    public static AgeRangesTable getAgeRangeTable() {
        return ageRangeTable;
    }

    public static CategoriesTable getCategoryTable() {
        return categoryTable;
    }

    public static CinecardsTable getCinecardTable() {
        return cinecardTable;
    }

    public static CinecardTypesTable getCinecardTypeTable() {
        return cinecardTypeTable;
    }

    public static PeriodsTable getPeriodTable() {
        return periodTable;
    }

    public static RatesTable getRateTable() {
        return rateTable;
    }

    public static ShowingsTable getShowingTable() {
        return showingTable;
    }

    public static TicketsTable getTicketTable() {
        return ticketTable;
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

    public static void showRates() throws IOException {
        app.showRates();
    }

    public static void allert() {
        Alert allert = new Alert(AlertType.WARNING);
        allert.setHeaderText("Inserisci tutti i campi contrassegnati da *");
        allert.show();
    }

    public static void allertNotExist(String message) {
        Alert allert = new Alert(AlertType.ERROR);
        allert.setHeaderText(message);
        allert.show();
    }

}