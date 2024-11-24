import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subtasks = new ArrayList<>();
    }

    public void rmvSubtaskById(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }
    public void rmvAllSubtasks() {
        subtasks.clear();
    }
    public void addSubtask(Integer subtaskId) {
        subtasks.add(subtaskId);
    }
    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<>(subtasks);
    }
}
