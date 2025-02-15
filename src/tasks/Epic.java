package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
public class Epic extends Task {

    private final ArrayList<Integer> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW, null, null);
        this.subtasks = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void rmvSubtaskById(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void rmvAllSubtasks() {
        subtasks.clear();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask.id);
    }

    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<>(subtasks);
    }
}