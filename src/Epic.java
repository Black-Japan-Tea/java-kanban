import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        status = Status.NEW;
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, int id, ArrayList<Integer> subtask) {
        super(name, description, status, id);
        this.subtasks = subtask;
    }
}
