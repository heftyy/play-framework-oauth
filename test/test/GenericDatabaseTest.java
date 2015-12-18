package test;

import org.junit.After;
import org.junit.Before;
import play.db.Database;
import play.db.Databases;

public class GenericDatabaseTest extends GenericTest {
    protected Database database;

    @Before
    public void createDatabase() {
        database = Databases.createFrom(
                "org.postgresql.Driver",
                "postgres://postgres:qwerty@192.168.17.17:5432/frogger"
        );
    }

    @After
    public void shutdownDatabase() {
        database.shutdown();
    }
}
