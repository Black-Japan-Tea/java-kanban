import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int nextId = 1;


    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }


    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }
    public void updTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }
    public void rmvTasks() {
        tasks.clear();
    }


    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }
    public void updEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic uEpic = epics.get(epic.getId());
            uEpic.setName(epic.getName());
            uEpic.setDescription(epic.getDescription());
        }
    }
    private void updEpicStatus(Epic epic) {
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Integer subtaskId : epic.getSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() != Status.NEW) {
                isAllNew = false;
            } if (subtask.getStatus() != Status.DONE) {
                isAllDone = false;
            }
        }
        if (subtasks.isEmpty() || isAllNew) {
            epic.status = Status.NEW;
        } else if (isAllDone) {
            epic.status = Status.DONE;
        } else {
            epic.status = Status.IN_PROGRESS;
        }
    }
    public void rmvEpics() {
        epics.clear();
        subtasks.clear();
    }


    public void addSubtask(Subtask subtask) {
        if (epics.get(subtask.getEpicId()) == null) {
            return;
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getEpicId());
        updEpicStatus(epic);
    }
    public void updSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
        } if (Objects.equals(subtasks.get(subtask.getId()).getEpicId(), subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            updEpicStatus(epics.get(subtask.getEpicId()));
        }
    }
    public void rmvSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.rmvAllSubtasks();
            updEpicStatus(epic);
        }
    }


    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Task> getEpicsList() {
        return new ArrayList<>(epics.values());
    }
    public ArrayList<Task> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }


    public void rmvById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(id);
            epic.rmvSubtaskById(id);
            updEpicStatus(epic);
        } else {
            System.out.println("Отсутствует задача с таким ID.");
        }
    }
    public Task getTaskById(int id) {
        return tasks.get(id);
    }
    public Epic getEpicById(int id) {
        return epics.get(id);
    }
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }
    public ArrayList<Subtask> getSubtasksListByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtasksListByEpic = new ArrayList<>();
        if (epic != null) {
            for (Integer id : epic.getSubtasks()) {
                subtasksListByEpic.add(subtasks.get(id));
            }
        }
        return subtasksListByEpic;
    }

}
