package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasksEpic = new ArrayList<>();
    private List<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        setDuration(Duration.ofMinutes(0));
        setStartTime(LocalDateTime.MIN);
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTime;
        if (subtasksEpic.isEmpty()) {
            endTime = startTime;
        } else {
            endTime = subtasksEpic.getFirst().getStartTime().plus(subtasksEpic.getFirst().getDuration());
        }
        for (Subtask subtask : subtasksEpic) {
            if (subtask.getStartTime().plus(duration).isAfter(endTime)) {
                endTime = subtask.getStartTime().plus(duration);
            }
        }
        return endTime;
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

//    public void setAllSubtasks(List<Subtask> newSubtasksEpic) {
//        duration = Duration.ofMinutes(0);
//        this.subtasksEpic = newSubtasksEpic;
//        searchStartTime();
//        newSubtasksEpic.stream()
//                .peek(subtask -> duration = Duration.ofMinutes(subtask.duration.toMinutes() + duration.toMinutes()))
//                .findFirst();
//    }
//
//    private void searchStartTime() {
//        LocalDateTime minTime = getEndTime();
//        for (Subtask subtask : subtasksEpic) {
//            if (subtask.getStartTime().isBefore(minTime)) {
//                minTime = subtask.getStartTime();
//            }
//        }
//        startTime = minTime;
//    }

    public List<Subtask> getSubtasksEpic() {
        return subtasksEpic;
    }
}
