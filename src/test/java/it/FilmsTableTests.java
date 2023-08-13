package it;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

import db.ConnectionProvider;
import db.tables.FilmsTable;
import model.Film;

class FilmsTableTests {
    final static String username = "root";
    final static String password = "";
    final static String dbName = "cinema";
    
    final static ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    final static FilmsTable FilmsTable = new FilmsTable(connectionProvider.getMySQLConnection());

    //final Film film1 = new Film(1, "Elemental", "Cavalieri", Utils.buildDate(11, 10, 1998));
    final Film film2 = null; /*new Film(2, "Emily", "Frances O'Connor", 130, 2023,
                                "Cosa si nasconde dietro la creazione di un capolavoro? Emily racconta l'appassionante vita di una delle scrittrici più amate di tutti i tempi, Emily Bronte, mentre trova la sua voce letteraria e scrive uno dei pi\u00F9 importanti classici della letteratura, Cime tempestose.",
                                new Period(Utils.buildDate(3,7,2023).get(), Utils.buildDate(5, 7, 2023).get()),
                                new Genre("Drammatico-Biografico"));

    @BeforeEach
    void setUp() throws Exception {
        FilmsTable.dropTable();
        FilmsTable.createTable();
    }

    @AfterEach
    void tearDown() throws Exception {
        FilmsTable.dropTable();
    }

    @Test
    void creationAndDropTest() {
        assertTrue(FilmsTable.dropTable());
        assertFalse(FilmsTable.dropTable());
        assertTrue(FilmsTable.createTable());
        assertFalse(FilmsTable.createTable());
    }
    
    @Test
    void saveTest() {
        //assertTrue(FilmsTable.save(film1));
        //assertFalse(FilmsTable.save(film1));
        assertTrue(FilmsTable.save(film2));
    }
    
    @Test
    void updateTest() {
        //assertFalse(FilmsTable.update(film1));
        FilmsTable.save(film2);
        final Film updatedfilm2 = new Film(2, "Emily", "Frances O'Connor", 230, 2023,
                                "Cosa si nasconde dietro la creazione di un capolavoro? Emily racconta l'appassionante vita di una delle scrittrici più amate di tutti i tempi, Emily Bronte, mentre trova la sua voce letteraria e scrive uno dei pi\u00F9 importanti classici della letteratura, Cime tempestose.",
                                new Period(Utils.buildDate(3,7,2023).get(), Utils.buildDate(5, 7, 2023).get()),
                                new Genre("Drammatico-Biografico"));
        assertTrue(FilmsTable.update(updatedfilm2));
        final Optional<Film> foundFilm = FilmsTable.findByPrimaryKey(updatedfilm2.getId());
        assertFalse(foundFilm.isEmpty());
        assertEquals(updatedfilm2.getDuration(), foundFilm.get().getDuration());
    }*/

    /*@Test
    void deleteTest() {
        FilmsTable.save(film1);
        assertTrue(FilmsTable.delete(f.getId()));
        assertFalse(FilmsTable.delete(f.getId()));
        assertTrue(FilmsTable.findByPrimaryKey(f.getId()).isEmpty());
    }*/

    @Test
    void findByPrimaryKeyTest() {
        //FilmsTable.save(film1);
        FilmsTable.save(film2);
        //assertEquals(film1, FilmsTable.findByPrimaryKey(f.getId()).orElse(null));
        assertEquals(film2, FilmsTable.findByPrimaryKey(film2.getId()).orElse(null));
    }

    @Test
    void findAllTest() {
        //FilmsTable.save(film1);
        FilmsTable.save(film2);
        assertIterableEquals(
            //List.of(film1, film2),
            List.of(film2),
            FilmsTable.findAll()
        );
    }
}
