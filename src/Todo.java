import hcmus.adp.damproject.annotations.PrimaryField;
import hcmus.adp.damproject.annotations.Row;

@Row
public class Todo {
    @PrimaryField int id;
    String title;
    String content;
    boolean done;

    public Todo(
        int id,
        String title,
        String content,
        boolean done
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.done = done;
    }
}
