package hcmus.adp.damproject.session;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hcmus.adp.damproject.statement.SelectStatement;

public class Session {
    private Connection _conn;
    private Class<?> _dataClass;

    public Session(Connection conn, Class<?> dataclass) {
        this._conn = conn;
        this._dataClass = dataclass;
    }

    // -> List<Entity>
    public void select(SelectStatement stmt) throws SQLException {
        if (stmt.getDataClass().getName().equals(this._dataClass.getName())) {
            Statement _statement = this._conn.createStatement();
            ResultSet rs = _statement.executeQuery(stmt.toString());
            while (rs.next()) {
                System.out.println(rs.getInt("id"));
                System.out.println(rs.getString("content"));
            }
        } else {
            throw new SQLException();
        }
    }

    public void insert() {

    }

    /**
     * Execute whatever the hell you threw at this Session.
     * Exception-free-ness NOT guaranteed.
     * @param sqlQuery
     */
    public void executeSql(String sqlQuery) {

    }
}
