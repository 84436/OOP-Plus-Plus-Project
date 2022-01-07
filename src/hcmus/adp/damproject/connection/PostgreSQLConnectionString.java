package hcmus.adp.damproject.connection;

public class PostgreSQLConnectionString implements DBConnectionString {
    private String hostname;
    private int port;
    private String database;
    PostgreSQLConnectionString(String hostname, int port, String database) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
    }
    public String toString() {
        return String.format(
            "jdbc:postgresql://%s:%d/%s",
            this.hostname, this.port, this.database);
    }
}
