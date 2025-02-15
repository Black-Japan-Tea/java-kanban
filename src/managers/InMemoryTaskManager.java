package managers;

import java.util.*;
import java.time.LocalDateTime;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));
    private final HistoryManager historyManager;
    protected Integer nextTaskId = 1;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
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
        for (Integer id : tasks.keySet()) {
            historyManager.rmv(id);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.rmv(id);
        }
        for (Integer id : epics.keySet()) {
            historyManager.rmv(id);
        }
        tasks.clear();
        subtasks.clear();
        epics.clear();
        nextTaskId = 0;
    }

    @Override
    public void rmvAllTasks() {
        for (Task task : tasks.values()) {
            if (historyManager.getHistory().contains(task)) {
                historyManager.rmv(task.getId());
            }
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void rmvAllEpics() {
        epics.values()
                .forEach(epic -> {
                    if (prioritizedTasks.contains(epic)) {
                        prioritizedTasks.remove(epic);
                    }
                    if (historyManager.getHistory().contains(epic)) {
                        epic.getSubtasksEpic()
                                .forEach(subtask -> {
                                    if (historyManager.getHistory().contains(subtask)) {
                                        historyManager.rmv(subtask.getId());
                                    }
                                });
                        historyManager.rmv(epic.getId());
                    }
                });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void rmvAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.rmvAllSubtasks();
            refreshEpicStatus(epic);
        }

        for (Integer taskId : subtasks.keySet()) {
            historyManager.rmv(taskId);
        }
        subtasks.clear();
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);

        return Optional.ofNullable(task);
    }

    @Override
    public Optional<Subtask> getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return Optional.ofNullable(subtask);
    }

    @Override
    public Optional<Epic> getEpicById(Integer id) {

        Epic epic = epics.get(id);
        historyManager.add(epic);
        return Optional.ofNullable(epic);
    }

    @Override
    public int addTask(Task newTask) {
        if (newTask == null) {
            return -1;
        }
        newTask.setId(nextTaskId);
        if (isNotIntersectionContain(newTask)) {
            prioritizedTasks.add(newTask);
        }
        tasks.put(newTask.getId(), newTask);
        nextTaskId++;
        return newTask.getId();
    }

    @Override
    public int addEpic(Epic newEpic) {
        if (newEpic == null) {
            return -1;
        }
        newEpic.setId(nextTaskId);
        epics.put(newEpic.getId(), newEpic);
        nextTaskId++;
        return newEpic.getId();
    }

    @Override
    public int addSubtask(Subtask newSubtask) {
        if (newSubtask == null) {
            return -1;
        }
        if (epics.get(newSubtask.getEpicId()) == null) {
            return -1;
        }
        newSubtask.setId(nextTaskId);
        subtasks.put(newSubtask.getId(), newSubtask);
        nextTaskId++;
        if (isNotIntersectionContain(newSubtask)) {
            prioritizedTasks.add(newSubtask);
        }
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addSubtask(newSubtask.getId());
        refreshEpicStatus(epic);
        return newSubtask.getId();
    }

    @Override
    public boolean updTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return false;
        }
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        if (isNotIntersectionContain(task)) {
            prioritizedTasks.add(task);
        }
        return true;
    }

    @Override
    public boolean updEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return false;
        }
        Epic epicToUpdate = epics.get(epic.getId());
        prioritizedTasks.remove(epics.get(epic.getId()));
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
        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        subtasks.put(subtask.getId(), subtask);
        refreshEpicStatus(epics.get(subtask.getEpicId()));
        if (isNotIntersectionContain(subtask)) {
            prioritizedTasks.add(subtask);
        }
        return true;
    }

    @Override
    public boolean rmvTaskById(Integer id) {
        if (!tasks.containsKey(id)) {
            return false;
        }
        tasks.remove(id);
        historyManager.rmv(id);
        return true;
    }

    @Override
    public boolean rmvEpicById(Integer id) {
        if (!epics.containsKey(id)) {
            return false;
        }
        Epic epic = epics.get(id);
        if (!epic.getSubtasks().isEmpty()) {
            epic.getSubtasksEpic().forEach(subtask -> {
                subtasks.remove(subtask.getId());
                if (historyManager.getHistory().contains(subtask)) {
                    historyManager.rmv(subtask.getId());
                }
            });
        }
        epics.remove(id);
        historyManager.rmv(id);
        return true;
    }

    @Override
    public List getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean rmvSubtaskById(Integer id) {
        if (!subtasks.containsKey(id)) {
            return false;
        }
        Subtask subTask = subtasks.get(id);
        subtasks.remove(id);
        historyManager.rmv(id);
        Epic subtaskEpic = epics.get(subTask.getEpicId());
        subtaskEpic.rmvSubtaskById(id);
        refreshEpicStatus(subtaskEpic);
        return true;
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (epic != null) {
            epic.getSubtasks().stream()
                    .map(this::getSubtaskById)
                    .forEach(optionalSubTask -> optionalSubTask.ifPresent(epicSubtasks::add));

        }
        return epicSubtasks;
    }

    private void refreshEpicStatus(Epic epic) {
        ArrayList<Integer> epicSubtasks = epic.getSubtasks();
        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : epicSubtasks) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }

        }
        if (subtasks.isEmpty() || allNew) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }

    protected void loadTask(Task task) {
        tasks.put(task.getId(), task);
    }

    protected void loadSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        refreshEpicStatus(epic);
    }

    protected void loadEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected Integer getLastId() {
        return this.nextTaskId - 1;
    }

    protected void setLastId(Integer lastId) {
        this.nextTaskId = lastId + 1;
    }

    private boolean isTimeSegmentsIntersect(Task newTask, Task task) {
        LocalDateTime startsTimeNewTask = newTask.getStartTime();
        LocalDateTime finishTimeNewTask = newTask.getEndTime();
        LocalDateTime startsTimeTask = task.getStartTime();
        LocalDateTime finishTimeTask = task.getEndTime();
        return !startsTimeNewTask.isAfter(finishTimeTask) && !finishTimeNewTask.isBefore(startsTimeTask);
    }

    private boolean isNotIntersectionContain(Task newTask) {
        return prioritizedTasks.stream()
                .noneMatch(task -> isTimeSegmentsIntersect(newTask, task));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
