package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager;
    private Integer nextTaskId = 1;

    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {

        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void rmvAllTypesOfTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        nextTaskId = 0;
    }

    @Override
    public void rmvAllTasks() {
        tasks.clear();
    }

    @Override
    public void rmvAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void rmvAllSubtasks() {
        for (Epic epic : epics.values()
        ) {
            epic.rmvAllSubtasks();
            refreshEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer id) {

        Epic epic = epics.get(id);
        historyManager.add(epic);

        return epic;
    }

    @Override
    public void addTask(Task newTask) {
        if (newTask == null) {
            return;
        }
        newTask.setId(nextTaskId);
        tasks.put(newTask.getId(), newTask);
        nextTaskId++;
    }

    @Override
    public void addEpic(Epic newEpic) {
        if (newEpic == null) {
            return;
        }
        newEpic.setId(nextTaskId);
        epics.put(newEpic.getId(), newEpic);
        nextTaskId++;
    }

    @Override
    public void addSubtask(Subtask newSubtask) {
        if (newSubtask == null) {
            return;
        }
        if (epics.get(newSubtask.getEpicId()) == null) {
            return;
        }
        newSubtask.setId(nextTaskId);
        subtasks.put(newSubtask.getId(), newSubtask);
        nextTaskId++;
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addSubtask(newSubtask.getId());
        refreshEpicStatus(epic);
    }

    @Override
    public boolean updTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return false;
        }
        tasks.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean updEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return false;
        }
        Epic epicToUpdate = epics.get(epic.getId());
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setDescription(epic.getDescription());

        return true;
    }

    @Override
    public boolean updSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return false;
        }
        if (!Objects.equals(subtasks.get(subtask.getId()).getEpicId(), subtask.getEpicId())) {
            return false;
        }
        subtasks.put(subtask.getId(), subtask);
        refreshEpicStatus(epics.get(subtask.getEpicId()));
        return true;
    }

    @Override
    public boolean rmvTaskById(Integer id) {
        if (!tasks.containsKey(id)) {
            return false;
        }
        tasks.remove(id);
        return true;
    }

    @Override
    public boolean rmvEpicById(Integer id) {
        if (!epics.containsKey(id)) {
            return false;
        }
        Epic epic = epics.get(id);
        if (epic.getSubtasks().size() != 0) {
            for (Integer subtaskId : epic.getSubtasks()
            ) {
                subtasks.remove(subtaskId);
            }
        }
        epics.remove(id);
        return true;
    }

    @Override
    public boolean rmvSubtaskById(Integer id) {
        if (!subtasks.containsKey(id)) {
            return false;
        }

        Subtask subtask = subtasks.get(id);
        subtasks.remove(id);
        Epic subtaskEpic = getEpicById(subtask.getEpicId());
        subtaskEpic.rmvSubtaskById(id);
        refreshEpicStatus(subtaskEpic);
        return true;
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {

        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (epic != null) {
            for (
                    Integer id : epic.getSubtasks()
            ) {
                epicSubtasks.add(getSubtaskById(id));
            }
        }
        return epicSubtasks;
    }

    private void refreshEpicStatus(Epic epic) {
        ArrayList<Integer> epicSubtasks = epic.getSubtasks();
        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : epicSubtasks
        ) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }

        }
        if (subtasks.size() == 0 || allNew) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }
}
