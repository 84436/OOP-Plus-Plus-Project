package hcmus.adp.damproject.connection;

public class SQLiteFileConnectionString implements DBConnectionString {
    private String filename;
    public SQLiteFileConnectionString(String filename) {
        this.filename = filename;
    }
    public String toString() {
        return String.format(
            "jdbc:sqlite:%s",
            this.filename);
    }
}
