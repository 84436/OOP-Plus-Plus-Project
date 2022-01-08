import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import hcmus.adp.damproject.connection.DBConnection;
import hcmus.adp.damproject.connection.SQLiteFileConnectionString;
import hcmus.adp.damproject.entity.Entity;
import hcmus.adp.damproject.enums.AGGREGATE;
import hcmus.adp.damproject.enums.OP_LOGIC;
import hcmus.adp.damproject.enums.OP_RELATION;
import hcmus.adp.damproject.session.Session;
import hcmus.adp.damproject.statement.CondStmt;
import hcmus.adp.damproject.statement.LogicCondStmt;
import hcmus.adp.damproject.statement.SelectStatementBuilder;

public class App {
    public static String stringifyTodo(Entity t) {
        return String.format(
                "%s Item #%d: %s (%s)",
                (boolean) t.get("done") ? "[x]" : "[ ]",
                t.get("id"),
                t.get("title"),
                t.get("content"));
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBConnection.connect(
                    new SQLiteFileConnectionString("data.db"));

            Session mySession = new Session(conn, Todo.class);

            // INSERT
            int counter = 1;
            while (counter <= 5) {
            mySession.insert(new Todo(
            counter,
            String.format("This is todo item %d", counter),
            String.format("Description %d", counter),
            counter % 2 == 0 ? true : false
            ));
            counter += 1;
            }

            // SELECT (id >= 2 && id <= 4)
            ArrayList<Entity> filteredTodos = mySession.select(
            new SelectStatementBuilder()
            .from(Todo.class)
            .where(
            new CondStmt("id", OP_RELATION.EQ, 2))
            .getResult()
            );
            for (var each: filteredTodos) {
            System.out.println(String.format(
            "id=%d, title=%s",
            each.get("id"), each.get("title")
            ));
            }

            // SELECT (all)
            ArrayList<Entity> todos = mySession.select(
            new SelectStatementBuilder()
            .from(Todo.class)
            .getResult()
            );
            for (var each: todos) { System.out.println(stringifyTodo(each)); }

            // UPDATE
            Entity myFirstTodo = todos.get(1);
            boolean updated = myFirstTodo.set("content", "ホー・トアン・タン先生は、世界で一番ハンサムな先生だよ~");
            if (updated) {
            System.out.println("Todo updated successfully:");
            System.out.println(stringifyTodo(myFirstTodo));
            }

            // DELETE
            boolean isDeleted = false;
            boolean isDeleteCommandExecuted = false;

            System.out.println("Before delete");
            isDeleted = todos.get(4).isDeleted();
            System.out.println(isDeleted);

            System.out.println("While delete");
            isDeleteCommandExecuted = todos.get(4).delete();
            System.out.println(isDeleteCommandExecuted);

            System.out.println("After delete");
            isDeleted = todos.get(4).isDeleted();
            System.out.println(isDeleted);

            // CREATE select statement with group by
            SelectStatementBuilder selectStatementBuilder = new SelectStatementBuilder();
            selectStatementBuilder.from(Todo.class);
            selectStatementBuilder.groupby("done");
            selectStatementBuilder.setAggregate(AGGREGATE.SUM);
            selectStatementBuilder.setAggregateCol("id");
            System.out.println(selectStatementBuilder.getResult().toString());
            Session a = new Session(conn, Todo.class);
            HashMap<Entity, Float> res = a.aggregate(selectStatementBuilder.getResult());
            for(var entity:res.entrySet()){
               System.out.println(entity.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
