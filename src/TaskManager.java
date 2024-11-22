import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;
    int nextId = 1;


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
        tasks.put(task.getId(), task);
    }
    public void rmvTasks() {
        tasks.clear();
    }


    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }
    public void updEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    private void updEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Integer subtaskId : epic.subtasks) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.status != Status.NEW) {
                isAllNew = false;
            } if (subtask.status != Status.DONE) {
                isAllDone = false;
            }
        }
        if (isAllDone) {
            epic.status = Status.DONE;
        } else if (isAllNew) {
            epic.status = Status.NEW;
        } else {
            epic.status = Status.IN_PROGRESS;
        }
    }
    public void rmvEpics() {
        epics.clear();
        subtasks.clear();
    }


    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.subtasks.add(subtask.getId());
        updEpicStatus(subtask.getEpicId());
    }
    public void updSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updEpicStatus(subtask.getEpicId());
    }
    public void rmvSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.subtasks.clear();
            epic.status = Status.NEW;
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
            for (Integer subtaskId : epic.subtasks) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(id);
            epic.subtasks.remove(Integer.valueOf(id));
            updEpicStatus(epic.getId());
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
        for (Integer id : epic.subtasks) {
            subtasksListByEpic.add(subtasks.get(id));
        }
        return subtasksListByEpic;
    }

}
