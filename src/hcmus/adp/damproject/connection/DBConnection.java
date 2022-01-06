package hcmus.adp.damproject.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection _connInstance;
    private DBConnection() {}
    public static Connection connect(DBConnectionString cs) throws SQLException {
        if (_connInstance == null) {
            _connInstance = DriverManager.getConnection(cs.toString());
        }
        return _connInstance;
    }
    public static void disconnect() throws SQLException {
        _connInstance.close();
    }
}
