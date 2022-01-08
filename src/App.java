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
import hcmus.adp.damproject.statement.SelectStatement;
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
            /**
             * Connect to database and create session
             */
            conn = DBConnection.connect(
                    new SQLiteFileConnectionString("data.db"));

            Session mySession = new Session(conn, Todo.class);

            /**
             * INSERT
             */
            System.out.println("INSERT");
            int counter = 1;
            boolean insertSucceeded = false;
            while (counter <= 5) {
                insertSucceeded = mySession.insert(new Todo(
                    counter,
                    String.format("This is todo item %d", counter),
                    String.format("Description %d", counter),
                    counter % 2 == 0 ? true : false
                ));
                if (insertSucceeded) {
                    System.out.println(String.format("Item #%d inserted successfully", counter));
                } else {
                    System.out.println(String.format("Item #%d failed to insert", counter));
                }
                counter += 1;
            }
            System.out.println();

            /**
             * SELECT with condition
             */
            System.out.println("SELECT with condition");
            ArrayList<Entity> filteredTodos = mySession.select(
                new SelectStatementBuilder()
                    .from(Todo.class)
                    .where(
                        new LogicCondStmt(
                            new CondStmt("id", OP_RELATION.GE, 2),
                            OP_LOGIC.AND,
                            new CondStmt("id", OP_RELATION.LE, 4)
                        )
                    )
                    .getResult()
                );
            for (var each: filteredTodos) {
                System.out.println(String.format(
                    "id=%d, title=%s",
                    each.get("id"), each.get("title")
                ));
            }
            System.out.println();

            /**
             * SELECT all
             */
            System.out.println("SELECT all");
            ArrayList<Entity> todos = mySession.select(
                new SelectStatementBuilder()
                    .from(Todo.class)
                    .getResult()
                );
            for (var each: todos) {
                System.out.println(stringifyTodo(each));
            }
            System.out.println();

            /**
             * UPDATE
             */
            System.out.println("UPDATE");
            Entity myFirstTodo = todos.get(1);
            boolean updated = myFirstTodo.set("content", "ホー・トアン・タン先生は、世界で一番ハンサムな先生だよ~");
            if (updated) {
                System.out.println("Todo updated successfully:");
            } else {
                System.out.println("Todo failed to update:");
            }
            System.out.println(stringifyTodo(myFirstTodo));
            System.out.println();

            /**
             * DELETE
             */
            System.out.println("DELETE");
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
            System.out.println();

            /**
             * SELECT with GROUP BY
             */
            System.out.println("SELECT with GROUP BY");
            SelectStatement aggregateStmt = new SelectStatementBuilder()
                .from(Todo.class)
                .setAggregate(AGGREGATE.SUM)
                .setAggregateCol("id")
                .groupby("done")
                .getResult();
            System.out.println(aggregateStmt);
            HashMap<Entity, Float> res = mySession.aggregate(aggregateStmt);
            for (var entity: res.entrySet()) {
                System.out.println(entity.getValue());
            }
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
