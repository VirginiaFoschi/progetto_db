package it;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import db.ConnectionProvider;
import db.tables.PeriodsTable;
import model.Period;
import model.Genre;
import utils.Pair;
import utils.Utils;

class PeriodsTableTests {
    final static String username = "root";
    final static String password = "";
    final static String dbName = "cinema";
    
    final static ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    final static PeriodsTable PeriodsTable = new PeriodsTable(connectionProvider.getMySQLConnection());

    //final Period Period1 = new Period(1, "Elemental", "Cavalieri", Utils.buildDate(11, 10, 1998));
    final Period period = new Period(Utils.buildDate(3,7,2023).get(), Utils.buildDate(5, 7, 2023).get());

    @BeforeEach
    void setUp() throws Exception {
        PeriodsTable.dropTable();
        //PeriodsTable.createTable();
    }

    @AfterEach
    void tearDown() throws Exception {
        PeriodsTable.dropTable();
    }

    @Test
    void creationAndDropTest() {
        assertTrue(PeriodsTable.dropTable());
        assertFalse(PeriodsTable.dropTable());
        //assertTrue(PeriodsTable.createTable());
        //assertFalse(PeriodsTable.createTable());
    }
    
    @Test
    void saveTest() {
        //assertTrue(PeriodsTable.save(Period1));
        //assertFalse(PeriodsTable.save(Period1));
        assertTrue(PeriodsTable.save(period));
    }
    
    @Test
    void updateTest() {
        //assertFalse(PeriodsTable.update(Period1));
        PeriodsTable.save(period);
        final Period updatedperiod = new Period(Utils.buildDate(3,7,2023).get(), Utils.buildDate(5, 7, 2023).get());
        assertTrue(PeriodsTable.update(updatedperiod));
        final Optional<Period> foundPeriod = PeriodsTable.findByPrimaryKey(new Pair<>(updatedperiod.getStartDate(),updatedperiod.getEndDate()));
        assertFalse(foundPeriod.isEmpty());
        assertEquals(updatedperiod.getStartDate(), foundPeriod.get().getStartDate());
    }

    /*@Test
    void deleteTest() {
        PeriodsTable.save(Period1);
        assertTrue(PeriodsTable.delete(f.getId()));
        assertFalse(PeriodsTable.delete(f.getId()));
        assertTrue(PeriodsTable.findByPrimaryKey(f.getId()).isEmpty());
    }*/

    @Test
    void findByPrimaryKeyTest() {
        //PeriodsTable.save(Period1);
        PeriodsTable.save(period);
        //assertEquals(Period1, PeriodsTable.findByPrimaryKey(f.getId()).orElse(null));
        assertEquals(period, PeriodsTable.findByPrimaryKey(new Pair<>(period.getStartDate(),period.getEndDate())).orElse(null));
    }

    @Test
    void findAllTest() {
        //PeriodsTable.save(Period1);
        PeriodsTable.save(period);
        assertIterableEquals(
            //List.of(Period1, period),
            List.of(period),
            PeriodsTable.findAll()
        );
    }
}
