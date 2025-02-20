package manager_tests;

import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    Duration duration = Duration.ofMinutes(10);

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void shouldCorrectTask() {
        Task task = new Task("taskName", "taskDsc", Status.NEW, duration,
                LocalDateTime.of(2025, 2, 4, 15, 0));
        taskManager.addTask(task);

        Task savedTask = taskManager.getTaskById(task.getId());

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(task, savedTask);

        final Collection<Task> tasks = taskManager.getTasks();
        ArrayList<Task> list = new ArrayList<>(tasks);

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task, list.getFirst());

        taskManager.rmvAllTasks();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void shouldCorrectEpic() {
        Epic epic = new Epic("epicName", "epicDsc");
        int epicId = taskManager.addEpic(epic);

        Subtask thisEpicToSubtask = new Subtask(epic.getName(), epic.getDescription(), Status.NEW, epicId,
                duration, LocalDateTime.of(2025, 2, 5, 15, 0));
        thisEpicToSubtask.setId(epicId);

        assertEquals(-1, taskManager.addSubtask(thisEpicToSubtask));

        Epic savedEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(savedEpic);
        Assertions.assertEquals(epic, savedEpic);

        final Collection<Epic> epics = taskManager.getEpics();
        ArrayList<Task> list = new ArrayList<>(epics);

        Assertions.assertNotNull(epics);
        Assertions.assertEquals(1, epics.size());
        Assertions.assertEquals(epic, list.getFirst());

        taskManager.rmvAllEpics();
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void shouldCorrectSubtask() {
        Epic epic = new Epic("epicName", "epicDsc");
        int epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("subtaskName", "subtaskDsc", Status.NEW, epicId,
                duration, LocalDateTime.of(2025, 2, 6, 15, 0));
        taskManager.addSubtask(subtask);

        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());

        Assertions.assertNotNull(savedSubtask);
        Assertions.assertEquals(subtask, savedSubtask);

        final Collection<Subtask> subtasks = taskManager.getSubtasks();
        ArrayList<Task> list = new ArrayList<>(subtasks);

        Assertions.assertNotNull(subtasks);
        Assertions.assertEquals(1, subtasks.size());
        Assertions.assertEquals(subtask, list.getFirst());

        taskManager.rmvAllSubtasks();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void shouldSaveHistoryTask() {
        Task task1 = new Task("name", "dsc", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 4, 15, 0));
        Task task2 = new Task("name", "dsc", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 5, 15, 0));
        Task task3 = new Task("name", "dsc", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 6, 15, 0));
        Task task4 = new Task("name", "dsc", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 7, 15, 0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);

        Epic epic = new Epic("epicName", "epicDsc");
        int epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("subtaskName", "subtaskDsc", Status.NEW, epicId,
                Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 8, 15, 0));
        taskManager.addSubtask(subtask);

        taskManager.getTaskById(1);
        Task taskId2 = taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(1);
        taskManager.getEpicById(5);
        Task taskId6 = taskManager.getSubtaskById(6);

        Assertions.assertEquals(taskId2, taskManager.getHistory().getFirst());
        Assertions.assertNotNull(taskManager.getHistory());
        Assertions.assertEquals(6, taskManager.getHistory().size());
        Assertions.assertEquals(taskId6, taskManager.getHistory().getLast());

        taskManager.rmvEpicById(epicId);
        Assertions.assertEquals(4, taskManager.getHistory().size());
        Assertions.assertFalse(taskManager.getHistory().contains(subtask));

        taskManager.rmvAllEpics();
        Assertions.assertEquals(4, taskManager.getHistory().size());
    }

    @Test
    void shouldCorrectEpicStatus() {
        Epic epic = new Epic("epicName", "epicDsc");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDsc1", Status.NEW,
                epic.getId(), duration, LocalDateTime.of(2025, 2, 4, 15, 0));
        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDsc2", Status.NEW,
                epic.getId(), duration, LocalDateTime.of(2025, 2, 5, 15, 1));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        Assertions.assertEquals(Status.NEW, epic.getStatus());

        subtask1.setStatus(Status.DONE);
        taskManager.refreshEpicStatus(epic);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subtask2.setStatus(Status.DONE);
        taskManager.refreshEpicStatus(epic);
        Assertions.assertEquals(Status.DONE, epic.getStatus());

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.refreshEpicStatus(epic);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void shouldCalculateEpicsStartTimeAndDuration() {
        Epic epic = new Epic("epicName", "epicDsc");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDsc1", Status.NEW,
                epic.getId(), duration, LocalDateTime.of(2025, 2, 4, 15, 0));
        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDsc2", Status.NEW,
                epic.getId(), duration, LocalDateTime.of(2025, 2, 4, 17, 0));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        Assertions.assertEquals(LocalDateTime.of(2025, 2, 4, 15, 0), epic.getStartTime());
        Assertions.assertEquals(Duration.ofMinutes(20), epic.getDuration());
    }

    @Test
    void shouldAddGetPrioritizedTasks() {
        Task task = new Task("taskName", "taskDsc", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 4, 16, 0));
        taskManager.addTask(task);
        Epic epic = new Epic("epicName", "epicDsc");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtaskName", "subtaskDsc", Status.NEW,
                epic.getId(), Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 3, 15, 0));
        Subtask subtask2 = new Subtask("subtaskName", "subtaskDsc", Status.NEW,
                epic.getId(), Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 15, 0));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        Assertions.assertNotNull(taskManager.getPrioritizedTasks());
        Assertions.assertEquals(taskManager.getPrioritizedTasks().getFirst(), subtask1);
        Assertions.assertEquals(taskManager.getPrioritizedTasks().getLast(), task);
    }

    @Test
    void shouldGetNonIntersectedTasks() {
        Task task1 = new Task("taskName1", "taskDsc1", Status.NEW,
                duration, LocalDateTime.of(2025, 2, 4, 15, 0));
        Task task2 = new Task("taskName2", "taskDsc2", Status.NEW,
                duration, LocalDateTime.of(2025, 2, 4, 15, 0));
        int idTask1 = taskManager.addTask(task1);
        int idTask2 = taskManager.addTask(task2);
        Assertions.assertNull(taskManager.getTaskById(idTask2));
    }
}