package hcmus.adp.damproject.connection;

public class SQLiteRAMConnectionString implements DBConnectionString {
    public String toString() {
        return "jdbc:sqlite::memory:";
    }
}
