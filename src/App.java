import java.sql.Connection;
import java.sql.SQLException;

import hcmus.adp.damproject.annotations.PrimaryField;
import hcmus.adp.damproject.annotations.Row;
import hcmus.adp.damproject.connection.DBConnection;
import hcmus.adp.damproject.connection.SQLiteFileConnectionString;
import hcmus.adp.damproject.session.Session;
import hcmus.adp.damproject.statement.SelectStatement;
import hcmus.adp.damproject.statement.SelectStatementBuilder;

public class App {
    /**
     * 1. new Connection (global)
     * 2. Session(Connection, MyDataType)
     * 
     * Session.select() -> Entity:Data[]
     * Session.insert() -> Entity:Data (return whole object)
     * Entity.save() -> Entity:Data
     * Entity.delete() -> Entity:Date
     */

    @Row
    class Data {
        @PrimaryField
        int id;
        String content;
    }

    // and(A, or(and(D, B), C))
    // A: id >= 1
    // B: content != "Hello"
    // C: title = "World"
    // D: done != false

    // (id >= 1 AND ((done != false AND content != 'Hello') OR title = 'World'))

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBConnection.connect(
                    new SQLiteFileConnectionString("/home/arch/test.db"));
            
            Session mySession = new Session(conn, Data.class);

            SelectStatementBuilder builder = new SelectStatementBuilder();
            SelectStatement result = builder.from(Data.class).getResult();

            mySession.select(result);
        } catch (SQLException e) {
            System.out.println("Cannot connect to database.");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
