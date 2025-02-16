package managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Integer nextTaskId = 1;

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
    public void rmvAllTasks() {
        tasks.keySet()
                .forEach(id -> {
                    prioritizedTasks.remove(tasks.get(id));
                    historyManager.rmv(id);
                });

        tasks.clear();
    }

    @Override
    public void rmvAllSubtasks() {
        subtasks.keySet()
                .forEach(id -> {
                    prioritizedTasks.remove(subtasks.get(id));
                    historyManager.rmv(id);
                });
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.rmvAllSubtasks();
            refreshEpicStatus(epic);
            updEpicsStartTimeAndDuration(epic);
        });
    }

    @Override
    public void rmvAllEpics() {
        subtasks.keySet()
                .forEach(id -> {
                    prioritizedTasks.remove(subtasks.get(id));
                    historyManager.rmv(id);
                });

        epics.keySet()
                .forEach(historyManager::rmv);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public int addTask(Task newTask) {
        if (newTask == null) {
            return -1;
        }
        try {
            if (isIntersectTask(newTask)) {
                throw new IllegalArgumentException("'" + newTask.getName() + "'" + " пересекается по времени выполнения.");
            }
            newTask.setId(nextTaskId);
            tasks.put(newTask.getId(), newTask);
            addPrioritizedTasks(newTask);
            nextTaskId++;
            return newTask.getId();
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return -1;
        }
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
        try {
            if (epics.containsKey(newSubtask.getEpicId())) {
                if (isIntersectTask(newSubtask)) {
                    throw new IllegalArgumentException("'" + newSubtask.getName() + "'" + " пересекается по времени выполнения.");
                }
                if (newSubtask.getEpicId() == newSubtask.getId()) {
                    return -1;
                }
                newSubtask.setId(nextTaskId);
                subtasks.put(newSubtask.getId(), newSubtask);
                addPrioritizedTasks(newSubtask);
                nextTaskId++;

                Epic epic = epics.get(newSubtask.getEpicId());
                epic.addSubtask(newSubtask);
                refreshEpicStatus(epic);
                updEpicsStartTimeAndDuration(epic);
                return newSubtask.getId();
            }
            return -1;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public void updTask(Task newTask) {
        try {
            if (tasks.containsKey(newTask.getId())) {
                prioritizedTasks.remove(tasks.get(newTask.getId()));
                if (isIntersectTask(newTask)) {
                    addPrioritizedTasks(tasks.get(newTask.getId()));
                    throw new IllegalArgumentException("Обновленная '" + newTask.getName() + "' пересекается по времени выполнения");
                }
                tasks.put(newTask.getId(), newTask);
                addPrioritizedTasks(newTask);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void updEpic(Epic newEpic) {
        Epic oldEpic = epics.get(newEpic.getId());
        if (oldEpic != null) {
            oldEpic.setName(newEpic.getName());
            oldEpic.setDescription(newEpic.getDescription());
        }
    }

    @Override
    public void updSubtask(Subtask newSubtask) {
        try {
            if (subtasks.containsKey(newSubtask.getId())) {
                if (subtasks.get(newSubtask.getId()).getEpicId() == newSubtask.getEpicId()) {
                    prioritizedTasks.remove(subtasks.get(newSubtask.getId()));
                    if (isIntersectTask(newSubtask)) {
                        addPrioritizedTasks(subtasks.get(newSubtask.getId()));
                        throw new IllegalArgumentException("Обновленная '" + newSubtask.getName() + "' пересекается по времени выполнения");
                    }
                    subtasks.put(newSubtask.getId(), newSubtask);
                    addPrioritizedTasks(newSubtask);
                    Epic epic = epics.get(newSubtask.getEpicId());
                    refreshEpicStatus(epic);
                    updEpicsStartTimeAndDuration(epic);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void rmvTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.rmv(id);
    }

    @Override
    public void rmvEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubtasks()
                    .forEach(subtaskId -> {
                        prioritizedTasks.remove(subtasks.get(subtaskId));
                        subtasks.remove(subtaskId);
                        historyManager.rmv(subtaskId);
                    });
            epics.remove(id);
            historyManager.rmv(id);
        }
    }

    @Override
    public void rmvSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());

            epic.rmvSubtaskById(id);
            prioritizedTasks.remove(subtask);
            subtasks.remove(id);
            historyManager.rmv(id);
            refreshEpicStatus(epic);
            updEpicsStartTimeAndDuration(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Subtask> getSubtaskByEpicId(Epic epic) {
        return epic.getSubtasks().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    protected void addPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            getPrioritizedTasks().add(task);
        }
    }

    protected void updEpicsStartTimeAndDuration(Epic epic) {
        Collection<Subtask> subtasksByEpic = getSubtaskByEpicId(epic);
        long durationEpic = subtasksByEpic.stream()
                .map(Task::getDuration)
                .filter(Objects::nonNull)
                .mapToLong(Duration::toMinutes)
                .sum();
        if (durationEpic == 0) {
            epic.setDuration(null);
        }
        epic.setDuration(Duration.ofMinutes(durationEpic));

        LocalDateTime startTime = subtasksByEpic.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        epic.setStartTime(startTime);

        LocalDateTime endTime = subtasksByEpic.stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        epic.setEndTime(endTime);
    }

    protected void setNextTaskId(int nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

    public void refreshEpicStatus(Epic epic) {
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

        if (epics.containsKey(subtask.getEpicId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.addSubtask(subtask);
            refreshEpicStatus(epic);
            updEpicsStartTimeAndDuration(epic);
        }
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

    private boolean isIntersectTask(Task newTask) {
        return getPrioritizedTasks().stream()
                .anyMatch(existTask -> !(newTask.getEndTime().isBefore(existTask.getStartTime()) ||
                        newTask.getStartTime().isAfter(existTask.getEndTime())));
    }
}
